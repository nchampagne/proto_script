# Server-side JS executor

## Quickstart

```
sbt compile run
```

```
POST http://localhost:9000/answer
```

A client submits an answer for a question and includes the necessary validation logic in the payload.

Valid request body - both ```heightInFeet``` and ```heightInInches``` are within range.

```json
[
  {
    "questionId": "12345",
    "varMap": {
      "heightInFeet": {
        "id": "12015",
        "value": "6"
      },
      "heightInInches": {
        "id": "12016",
        "value": "1"
      }
    },
    "validationJs": "function evaluate(e,a){var u=JSON.parse(e),v=JSON.parse(a),l=!0,r=\"NONE\",s=[];return(u.value<0||u.value>8)&&s.push(u.value+\" is an invalid value for feet.\"),(v.value<0||v.value>12)&&s.push(v.value+\" is an invalid value for inches.\"),s.length>0?(l=!1,r=\"DISPLAY_ERROR\"):r=\"GO_TO\",{success:l,errors:s,directive:r}}var result=evaluate(heightInFeet,heightInInches);",
    "valueClearingJs": null
  }
]
```

Response

```json
{
  "12345": {
    "success": true,
    "values": {
      "heightInFeet": {
        "id": "12015",
        "value": "6"
      },
      "heightInInches": {
        "id": "12016",
        "value": "1"
      }
    },
    "directive": "GO_TO"
  }
}
```

Invalid request body

```json
[
  {
    "questionId": "12345",
    "varMap": {
      "heightInFeet": {
        "id": "12015",
        "value": "66"
      },
      "heightInInches": {
        "id": "12016",
        "value": "127"
      }
    },
    "validationJs": "function evaluate(e,a){var u=JSON.parse(e),v=JSON.parse(a),l=!0,r=\"NONE\",s=[];return(u.value<0||u.value>8)&&s.push(u.value+\" is an invalid value for feet.\"),(v.value<0||v.value>12)&&s.push(v.value+\" is an invalid value for inches.\"),s.length>0?(l=!1,r=\"DISPLAY_ERROR\"):r=\"GO_TO\",{success:l,errors:s,directive:r}}var result=evaluate(heightInFeet,heightInInches);",
    "valueClearingJs": null
  }
]
```

Response

```json
{
  "12345": {
    "success": false,
    "errors": [
      "66 is an invalid value for feet.",
      "127 is an invalid value for inches."
    ],
    "values": {
      "heightInFeet": {
        "id": "12015",
        "value": "66"
      },
      "heightInInches": {
        "id": "12016",
        "value": "127"
      }
    },
    "directive": "DISPLAY_ERROR"
  }
}
```

## Request body javascript

The ```validationJs``` string is a JSON-escaped compressed version of the following validation function. A ```result``` var must be defined at root level after an evaluation.

```javascript
// Must be at root level
var result = evaluate(heightInFeet, heightInInches);
function evaluate(heightInFeet, heightInInches) {
    var feet    = JSON.parse(heightInFeet);
    var inches  = JSON.parse(heightInInches);

    var success     = true;
    var directive   = 'NONE';
    var errors      = [];

    if(feet.value < 0 || feet.value > 8) {
        errors.push(feet.value + " is an invalid value for feet.");

    }
    if(inches.value < 0 || inches.value > 12) {
        errors.push(inches.value + " is an invalid value for inches.");
    }

    if(errors.length > 0) {
        success     = false;
        directive   = 'DISPLAY_ERROR';
    }
    else {
        directive = 'GO_TO';
    }

    var result = {
        success:    success,
        errors:     errors,
        directive:  directive
    };
    return result;
}
```

Results are transformed to a scala case class after execution.

```scala
case class ValidationResult(success:    Boolean,
                            errors:     Option[List[String]],
                            values:     Map[String, Datapoint],
                            directive:  String)
                            
case class Datapoint(id:    String,
                     value: String)
```