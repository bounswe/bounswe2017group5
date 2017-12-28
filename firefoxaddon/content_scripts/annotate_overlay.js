(function() {
    /**
     * Check and set a global guard variable.
     * If this content script is injected into the same page again,
     * it will do nothing next time.
     */
    if (window.hasRun) {
        return;
    }
    window.hasRun = true;

    var css = document.createElement("style");
    css.type = "text/css";
    css.innerHTML = `
    .anno-stored {
        box-shadow: 0 0 2px 1px tomato;
    }

    .anno-hover {
        box-shadow: 0 0 2px 1px yellowgreen;
    }
        
    .anno-selected {
        box-shadow: 0 0 3px 2px tomato;
        transition: box-shadow 0.2s ease-out;
    }

    .currently-unused::-moz-selection {
        background: tomato;
    }

    #anno-dismiss-button {
        background-color: firebrick;
        color: white;
        padding: 10px 20px;
        border-radius: 4px;
        border-color: darkred;

        position: fixed;
        bottom: -4px;
        right: 20px;
    }
    `;

    var dismissButton = document.createElement('button');
    dismissButton.id = 'anno-dismiss-button';
    dismissButton.innerText = 'Dismiss';

    var sidebarPort = browser.runtime.connect({name: "cs-sidebar"});

    // A URL parser, reference: https://stackoverflow.com/a/15979390
    var urlParser = document.createElement('a');
    function parseURL(url, value) {
        urlParser.href = url;
        return urlParser[value];
    }

    // https://stackoverflow.com/a/15724300
    function getCookie(name) {
        var value = "; " + document.cookie;
        var parts = value.split("; " + name + "=");
        if (parts.length == 2) return parts.pop().split(";").shift();
    }
    
    // https://stackoverflow.com/a/30265431
    function request(method, url, data) {
        return new Promise(function (resolve, reject) {
            var strData = [];
            var xhr = new XMLHttpRequest();
            xhr.open(method, url);

            xhr.onload = function () {
                if (this.status >= 200 && this.status < 300) {
                    resolve(xhr.response);
                }
                else {
                    reject({
                        status: this.status,
                        statusText: xhr.statusText
                    });
                }
            };

            xhr.onerror = function () {
                reject({
                    status: this.status,
                    statusText: xhr.statusText
                });
            };

            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.setRequestHeader('Accept', 'application/json');
            xhr.setRequestHeader('X-CSRFToken', getCookie('csrftoken'));
            
            if (data) {
                xhr.send(JSON.stringify(data));
            }
            else {
                xhr.send();
            }
        });
    }
    
    // https://stackoverflow.com/a/4588211, edited
    function CssSelector(el){
        var names = [];
        var i;
        var e;

        while (el.parentElement) {
            if (el.id) {
                names.push('#' + el.id);
                break;
            }
            else if (el === document.body) {
                names.push('body');
                break;
            }
            else {
                i = 1;
                e = el;
                while (e.previousElementSibling) {
                    i++;
                    e = e.previousElementSibling;
                }
                names.push(el.tagName + ":nth-child(" + i + ")");
            }

            el = el.parentElement;
        }

        return {
            type: 'CssSelector',
            value: names.reverse().join(" > ")
        };
    }

    function CSSPathTest() {
        for (var element of document.querySelectorAll('*')) {
            var value = CssSelector(element).value;
            if (element !== document.querySelector(value)) {
                alert('Failed with element: ' + value);
                break;
            }
        }
    }

    window.annoSelected = [];

    /**
     * Two functions for indicating the element selected
     */
    function annoUnselectAll() {
        window.annoSelected.forEach(element => {
            element.classList.remove('anno-selected');
        });

        window.annoSelected = [];
    }

    function selectedRange() {
        var selObj = window.getSelection();
        var selRange = selObj.getRangeAt(0);

        // if (selRange)
    }

    function handleClick(event) {
        annoUnselectAll();

        window.annoSelected.push(event.target);
        event.target.classList.add('anno-selected');
        sidebarPort.postMessage({
            elementClicked: {
                source: window.location.href,
                target: CssSelector(event.target),
                annotations: event.target.annotations
            }
        });

        event.stopPropagation();
        event.preventDefault();
        event.target.blur();
    }

    /**
     * Two functions for getting highlight over the elements
     */
    function handleMouseOver(event) {
        event.target.classList.add('anno-hover');
        event.stopPropagation();
    }
    
    function handleMouseOut(event) {
        event.target.classList.remove('anno-hover');
        event.stopPropagation();
    }

    function dismissOverlay() {
        document.body.removeChild(css);

        for (var element of document.querySelectorAll('*')) {
            element.onmouseover = element.originalOnmouseover;
            element.onmouseout = element.originalOnmouseout;
            element.onclick = element.originalOnclick;
            
            element.originalOnmouseover = undefined;
            element.originalOnmouseout = undefined;
            element.originalOnclick = undefined;

            element.annotations = undefined;
        }
        
        document.body.removeChild(dismissButton);
    }

    dismissButton.addEventListener('click', dismissOverlay);

    function injectAnnotation(annotation) {
        if (annotation.target === undefined) {
            console.log('Annotation target is not defined for:');
            console.log(annotation);
            return;
        }

        if (annotation.target.source !== window.location.href) {
            console.log('This annotation does not belong here:');
            console.log(annotation);
            return;
        }

        if (annotation.target.selector === undefined) {
            console.log('Annotation target does not have a selector:');
            console.log(annotation);
            return;
        }
        
        var sel = annotation.target.selector;
        var el;

        switch (sel.type) {
            case 'CssSelector':
                el = document.querySelector(sel.value);
                break;
            default:
                console.log(`'${sel.type}' is not supported as a selector type:`);
                console.log(annotation);
                return;
        }

        el.classList.add('anno-stored');
        el.annotations.push(annotation);
    }

    function injectAnnotations(annotations) {
        annotations.forEach(injectAnnotation);
    }

    function submitAnnotation(annotation) {
        const submissionURL = `${parseURL(window.location.href, 'origin')}/api/v1/annotations/`;
        // const submissionURL = '/annotations/'

        var selector = CssSelector(annoSelected[annoSelected.length - 1]);

        annotation.target = [{
            source: window.location.href,
            selector: selector
        }];

        if (selector.value.startsWith('#postbox-')) {
            annotation.target.push({
                source: `${parseURL(window.location.href, 'origin')}/search_advanced/`,
                selector: selector
            });
        }

        request('POST', submissionURL, annotation)
        .then((e) => {
            sidebarPort.postMessage({ submissionSuccessful: annotation });
            injectAnnotation(annotation);
        }, (e) => {
            alert('Failed to submit annotation: ' + submissionURL);
            
            console.log('Failed to submit annotation:');
            console.log(e);
            console.log(annotation);
        });
    }

    sidebarPort.onMessage.addListener(function (message) {
        if (message.submitButtonClicked) {
            submitAnnotation(message.submitButtonClicked);
        }
    });

    function filterAndMinifyAnnotation(annotation) {
        var ok = false;

        if (Array.isArray(annotation.target)) {
            for (var i = 0; i < annotation.target.length; i++) {
                if (annotation.target[i].source === window.location.href) {
                    annotation.target = annotation.target[i];
                    ok = true;
                    break;
                }
            }
        }
        else if (annotation.target.source === window.location.href) {
            ok = true;
        }

        if (ok) {
            return annotation;
        }

        return undefined;
    }

    function filterAndMinifyAnnotations(annotations) {
        for (var i = 0; i < annotations.length; i++) {
            annotations[i] = filterAndMinifyAnnotation(annotations[i]);
        }

        return annotations.filter(anno => { return anno; });
    }

    function retrieveAnnotations() {
        // var retrievalURL = `${parseURL(window.location.href, 'origin')}/api/v1/annotations/?source=${window.location.href}`;
        var retrievalURL = `${parseURL(window.location.href, 'origin')}/api/v1/annotations/`;
        // var retrievalURL = '/annotations/'

        request('GET', retrievalURL)
        .then((e) => {
            var annotations = JSON.parse(e);
            annotations = filterAndMinifyAnnotations(annotations);
            injectAnnotations(annotations);
        }, (e) => {
            console.log('Failed to retrieve annotations');
        });

        var annotations = [
            {
                "@context": "http://www.w3.org/ns/anno.jsonld",
                "id": "http://interestr.com/annotations/{id}",
                "type": "Annotation",
                "created": "2015-01-28T12:00:00Z",
                "creator": {
                    "id": "http://interestr.com/profile/anno1",
                    "type": "Person",
                    "name": "{firstname} {lastname}",
                    "nickname": "{username}",
                    "email": "{email}"
                },
              
                "bodyValue": "Love that place",
                "target": {
                    "source": "http://127.0.0.1:8000/groups/1/",
                    "type": "Text",
                    "selector": {
                        "type": "CssSelector",
                        "value": "div.group-detail-content:nth-child(4) > div:nth-child(2) > p:nth-child(1) > span:nth-child(2)"
                    }
                }
            },
            {
                "@context": "http://www.w3.org/ns/anno.jsonld",
                "id": "http://interestr.com/annotations/anno2",
                "type": "Annotation",
                "created": "2015-01-28T12:00:00Z",
                "creator": {
                    "id": "http://interestr.com/profile/1",
                    "type": "Person",
                    "name": "Utkan Gezer",
                    "nickname": "thoappelsin",
                    "email": "tho.appelsin@gmail.com"
                },
              
                "bodyValue": "Spent a night there once",
                "target": {
                    "source": "http://127.0.0.1:8000/groups/1/",
                    "type": "Text",
                    "selector": {
                        "type": "CssSelector",
                        "value": "div.group-detail-content:nth-child(4) > div:nth-child(2) > p:nth-child(1) > span:nth-child(2)"
                    }
                }
            }
        ];

        // injectAnnotations(annotations);
    }

    function applyOverlay() {
        document.body.appendChild(css);

        for (var element of document.body.querySelectorAll('*')) {
            element.originalOnmouseover = element.onmouseover;
            element.originalOnmouseout = element.onmouseout;
            element.originalOnclick = element.onclick;

            element.onmouseover = handleMouseOver;
            element.onmouseout = handleMouseOut;
            element.onclick = handleClick;

            element.annotations = [];
        }

        document.body.appendChild(dismissButton);

        retrieveAnnotations();
    }

    applyOverlay();
    
    // CSSPathTest();
    
})();
