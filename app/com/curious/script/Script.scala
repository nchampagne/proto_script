package com.curious.script

import javax.inject.Singleton
import javax.script.{ScriptContext, ScriptEngineManager, SimpleScriptContext}

import com.curious.model.{AnswerPayload, Datapoint}
import com.curious.util.MonadT
import jdk.nashorn.api.scripting.ScriptObjectMirror
import play.api.libs.json.Json

import com.curious.json.JsonFormats._
import scala.collection.JavaConversions._

case class ValidationResult(success:    Boolean,
                            errors:     Option[List[String]],
                            values:     Map[String, Datapoint],
                            directive:  String) extends MonadT[ValidationResult] {
  override protected def self = this
}

case class DisplayEvaluationResult(display: Boolean)

@Singleton
class ScriptEngine {

  def validateAnswers(answerPayloads: List[AnswerPayload]): Map[String, ValidationResult] = {
    answerPayloads.foldLeft(Map[String, ValidationResult]())((acc, a) => {
      val questionId        = a.questionId
      val varMap            = a.varMap
      val validationJs      = a.validationJs
      val validationResult  = validate(varMap, validationJs)
      acc + (questionId -> validationResult)
    })
  }

  def evaluateDisplayRules(varMap: Map[String, Datapoint], js: String): DisplayEvaluationResult = {
    val display = evaluateInternal[Boolean](varMap, js)
    DisplayEvaluationResult(display)
  }

  private def validate(varMap: Map[String, Datapoint], jsOpt: Option[String]): ValidationResult = {
    jsOpt.map { js =>
      val result    = evaluateInternal[ScriptObjectMirror](varMap, js)
      val success   = result.get("success").asInstanceOf[Boolean]
      val errors    = result.get("errors").asInstanceOf[ScriptObjectMirror].values().map( obj => obj.toString).toList
      val directive = result.get("directive").asInstanceOf[String]
      val optError = if(errors.size > 0) Some(errors) else None
      ValidationResult(success, optError, varMap, directive)
    }.getOrElse(ValidationResult(true, None, varMap, "NONE"))
  }

  private def evaluateInternal[A](varMap: Map[String, Datapoint], js: String): A = {
    val engine  = new ScriptEngineManager().getEngineByName("JavaScript")
    val context = new SimpleScriptContext();

    context.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);

    varMap.foreach { case (k, v) =>
      println(s"Adding $k -> ${v.value} to context")
      val jsonStr = Json.toJson(v).toString()
      context.setAttribute(k, jsonStr, ScriptContext.ENGINE_SCOPE)
    }
    // Execute
    engine.eval(js, context)
    // Return the result after evaluation
    context.getAttribute("result").asInstanceOf[A]
  }
}