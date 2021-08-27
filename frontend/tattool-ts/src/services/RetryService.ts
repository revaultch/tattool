/*
const retryPromiseUntil = (fnc, apply, condition, dlay) => {
  var interval = null;

  var test = async () => {
    var response = await fnc().then((response) => response);
    apply(response);
    if (condition(response)) {
      clearInterval(interval);
    }
  };

  interval = setInterval(test, dlay);
};

export { retryPromiseUntil };*/
