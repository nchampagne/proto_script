function shouldDisplayExceriseQuestion() {
    var display = evaluate(bmi);
    function evaluate(bmi) {
        var shouldDisplay = false;
        if(bmi >= 42) {
            shouldDisplay = true;
        }
        return shouldDisplay;
    }
}