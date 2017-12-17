package com.curious.json

import com.curious.model.{AnswerPayload, Datapoint}
import com.curious.script.ValidationResult
import play.api.libs.json.Json

object JsonFormats {
  implicit val DatapointJsonFormat = Json.format[Datapoint]
  implicit val AnswerPayloadJsonFormat  = Json.format[AnswerPayload]
  implicit val validationResultJsonFormat = Json.format[ValidationResult]
}
