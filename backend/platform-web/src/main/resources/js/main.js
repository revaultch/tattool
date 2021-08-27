/** An injectable javascript file for DOM analysis purposes */
(function () {
  /**template*/
  const uuidProperty = "qarts-uuid";

  const isVisible = function (el) {
    var style = window.getComputedStyle(el);
    return (
      style.display !== "none" &&
      style.visibility !== "hidden"
    );
  };

  function uuidv4() {
    return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, (c) =>
      (
        c ^
        (crypto.getRandomValues(new Uint8Array(1))[0] & (15 >> (c / 4)))
      ).toString(16)
    );
  }

  function getPathTo(element) {
    const idx = (sib, name) =>
      sib
        ? idx(sib.previousElementSibling, name || sib.localName) +
        (sib.localName == name)
        : 1;
    const segs = (elm) =>
      !elm || elm.nodeType !== 1
        ? [""]
        :
        elm.id ? [`//${elm.localName.toLowerCase()}[@id='${elm.id}']`] :
          [
            ...segs(elm.parentNode),
            `${elm.localName.toLowerCase()}[${idx(elm)}]`,
          ];
    return segs(element).join("/");
  }

  const escape = function (unsafe) {
    return unsafe
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  };

  class TattoolAnalysisHolder {
    content = {
      items: [],
      log: [],
    };

    refreshBounds() {
      var calcCumulativeOffset = function (el) {
        var _x = 0;
        var _y = 0;

        while (el && !isNaN(el.offsetLeft) && !isNaN(el.offsetTop)) {
          if (el.tagName.toLowerCase() != "body") {
            _x += el.offsetLeft - el.scrollLeft + el.clientLeft;
            _y += el.offsetTop - el.scrollTop + el.clientTop;
          } else {
            /** experimental -- added because of body offset not being calculated */
            const boundingClientRect = el.getBoundingClientRect();
            _x += boundingClientRect.x;
            _y += boundingClientRect.y;
          }
          el = el.offsetParent;
        }

        return { top: _y, left: _x };
      };

      try {
        var childElements = window.document.body.getElementsByTagName("*");
        for (var i = 0; i < childElements.length; i++) {
          var element = childElements[i];
          if (element.getAttribute && element.getAttribute(uuidProperty)) {
            var uuid = element.getAttribute(uuidProperty);
            var item = this.content.items.filter((item) => item.id == uuid)[0];
            if (item) {
              var positionInfo = element.getBoundingClientRect();
              var cumulativeOffset = calcCumulativeOffset(element);
              item.bounds.x = cumulativeOffset.left;
              item.bounds.y = cumulativeOffset.top;
              item.bounds.width = positionInfo.width;
              item.bounds.height = positionInfo.height;
            }
          }
        }
      } catch (e) {
        this.addToLog("error while setting bounds " + e);
      }
    }


    buildLocators(uuid) {

      const elAsText = (el) => {
        const tagName = el.tagName.toLowerCase();
        var value = "";
        if (tagName == 'select') {
          value = el?.options[el?.selectedIndex]?.text;
        } else if (tagName == 'input' || tagName == 'textarea') {
          value = el.value;
        } else {
          value = el.textContent;
        }
        if (value.length > 20) {
          value = value.substring(0, 20);
        }
        return value;
      };

      const buildIdLocator = (el) => ({ type: "id", query: el.getAttribute("id") });
      const buildTextLocator = (el) => ({
        type: "text",
        query:
          el.tagName.toLowerCase() + ":" + elAsText(el)
      });
      const buildXPathLocator = (el) => ({ type: "xpath", query: getPathTo(el) });

      try {
        var element = document.querySelectorAll(
          "[" + uuidProperty + '="' + uuid + '"]'
        )[0];

        return [
          buildIdLocator(element),
          buildTextLocator(element),
          buildXPathLocator(element),
        ];
      } catch (err) {
        this.addToLog(
          "error while building locators for " + uuid + "  : " + err
        );
      }
    }

    addElement(tags, element) {
      if (element == null) return;
      if (!isVisible(element)) return;

      const elementUuid =
        element.getAttribute && element.getAttribute(uuidProperty);

      if (!elementUuid) {
        const uuid = uuidv4();
        const item = {
          id: uuid,
          asString: element.outerHTML,
          screenshot: null,
          tags: [],
          bounds: {},
          locators: [],
        };
        /** push only those not tagged yet */
        item.tags.push.apply(item.tags, tags.filter((tag) => item.tags.indexOf(tag) == -1));
        element.setAttribute(uuidProperty, uuid);
        this.content.items.push(item);
      } else {
        const item = this.content.items.find((item) => item.id == elementUuid);
        if (item) {
          item.tags.push.apply(item.tags, tags);
        }
      }
    }

    removeElement(uuid) {
      var removeIndex = this.content.items.map((item) => item.id).indexOf(uuid);
      ~removeIndex && this.content.items.splice(removeIndex, 1);
      this.addToLog("removed " + uuid);
    }

    addToLog(e) {
      if (debug) {
        this.content.log.push(e);
      }
    }

    getContent() {
      return this.content;
    }
  }

  window.ttAnalysisHolder = new TattoolAnalysisHolder();
  window.ttAnalysisHolder.addToLog("initialized");

  const skip = ["html", "body", "option"];

  const tagName = (e) => e.tagName ? e.tagName.toLowerCase() : null;
  const hasTagName = (e, arr) => arr.find(item => item == tagName(e)) != null;
  const hasRole = (e, role) => e.role && e.role == role;
  const skippable = (e) => hasTagName(e, skip);

  const defaultEventMatchers = {
    "click": (e) => hasTagName(e, ["textarea", "input", "a", "select", "button"]) || hasRole("button"),
    "write": (e) => hasTagName(e, ["textarea", "input"]),
    "assert": (e) => hasTagName(e, ["select", "input", "textarea"]) || (e.textContent && e.textContent != "") || (e.innerText && e.innterText != ""),
    "select": (e) => hasTagName(e, ["select"]),
  };

  const handleNewElement = function (e) {
    try {
      if (!skippable(e)) {
        for (var key in defaultEventMatchers) {
          console.log(key + " ... " + defaultEventMatchers[key](e));
          if (defaultEventMatchers[key](e)) {
            window.ttAnalysisHolder.addElement([key], e);
          }
        }
      }
    } catch (err) {
      if (debug) {
        window.ttAnalysisHolder.addToLog(
          " error while adding new node : " + e + " : " + err
        );
      }
    }
  };

  const handleRemoveElement = function (e) {
    if (!e || !e.getAttribute) return;
    let uuid = e.getAttribute(uuidProperty);
    if (uuid) {
      window.ttAnalysisHolder.removeElement(uuid);
    }
  };

  /** intercept event listener registration */
  Element.prototype._attachEvent = Element.prototype.attachEvent;
  Element.prototype.attachEvent = function (a, b) {
    this._attachEvent(a, b);
    window.ttAnalysisHolder.addToLog(
      "attachEvent : " + a + " : " + escape(this.outerHTML)
    );
  };

  Element.prototype._addEventListener = Element.prototype.addEventListener;
  Element.prototype.addEventListener = function (a, b, c) {
    if (c == undefined) c = false;
    this._addEventListener(a, b, c);
    window.ttAnalysisHolder.addToLog(
      "addEvent : " + a + " : " + escape(this.outerHTML)
    );
    if (typeof a === "string" || a instanceof String) {
      window.ttAnalysisHolder.addElement([a, "event"], this);
    }
  };


  const handleNewHierarchy = (node) => {
    if (node && node.getElementsByTagName) {
      handleNewElement(node);
      if (node.getElementsByTagName) {
        var childElements = node.getElementsByTagName("*");
        for (var i = 0; i < childElements.length; i++) {
          var n = childElements[i];
          handleNewElement(n);
        }
      }
      /** note for shadow dom futur support -- experimental */
      if (node.shadowRoot) {
        var childElements = node.shadowRoot.children;
        for (var i = 0; i < childElements.length; i++) {
          var n = childElements[i];
          handleNewHierarchy(n);
        }
      }
    }
  };

  const handleOldHierarchy = (node) => {
    handleRemoveElement(node);
    if (node.getElementsByTagName) {
      var childElements = node.getElementsByTagName("*");
      for (var i = 0; i < childElements.length; i++) {
        var n = childElements[i];
        handleRemoveElement(n);
      }
    }
    /** note for shadow dom futur support -- experimental */
    if (node.shadowRoot) {
      var childElements = node.shadowRoot.children;
      for (var i = 0; i < childElements.length; i++) {
        var n = childElements[i];
        handleRemoveElement(n);
      }

    }

  };

  /** check for dom mutations and add or remove elements */
  var observer = new window.MutationObserver(function (mutations) {
    mutations.forEach(function (mutation) {
      var m = mutation;
      for (var j = 0; j < m.addedNodes.length; j++) {
        handleNewHierarchy(m.addedNodes[j]);
      }

      for (var j = 0; j < m.removedNodes.length; j++) {
        handleOldHierarchy(m.removedNodes[j])
      }
    });
  });

  var config = { childList: true, subtree: true };
  observer.observe(window.document, config);

  window.addEventListener("load", (event) => {
    /** load all elements at first */
    /**     const allElements = document.body.getElementsByTagName("*");
        for (var i = 0; i < allElements.length; i++) {
          handleNewElement(allElements[i]);
        }*/

  });

})();
