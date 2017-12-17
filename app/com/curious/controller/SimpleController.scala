package com.curious.controller

import javax.inject.Inject

import com.curious.model.AnswerPayload
import com.curious.script.{ScriptEngine, ValidationResult}
import com.curious.util.FutureAssist
import com.curious.json.JsonFormats._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

class SimpleController @Inject()(scriptEngine: ScriptEngine)(implicit ec: ExecutionContext) extends Controller with FutureAssist {

  def submitAnswers() = Action.async(parse.json[List[AnswerPayload]]) { request =>
    val validationResults: Map[String, ValidationResult] = scriptEngine.validateAnswers(request.body)
    asFuture(Ok(Json.toJson(validationResults)))
  }

  def requestQuestions(pageId: String) = {

  }
}
