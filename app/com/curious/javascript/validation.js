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
