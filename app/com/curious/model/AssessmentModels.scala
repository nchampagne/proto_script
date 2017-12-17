package com.curious.model

case class AnswerPayload(questionId:      String,
                         varMap:          Map[String, Datapoint],
                         validationJs:    Option[String],
                         valueClearingJs: Option[String])

case class Datapoint(id:    String,
                     value: String)
