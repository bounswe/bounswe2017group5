(function() {
    var css = document.createElement("style");
    css.type = "text/css";
    css.innerHTML = `
    .anno-highlight-hover {
        box-shadow: 0 0 2px 1px yellowgreen;
    }
    
    .anno-highlight-selected {
        box-shadow: 0 0 2px 4px tomato;
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

    var sidebarPort = browser.runtime.connect({name: "cs-to-sidebar"});

    /**
     * Check and set a global guard variable.
     * If this content script is injected into the same page again,
     * it will do nothing next time.
     */
    if (window.hasRun) {
        return;
    }
    window.hasRun = true;
    
    /**
     * Given a URL to a beast image, remove all existing beasts, then
     * create and style an IMG node pointing to
     * that image, then insert the node into the document.
     */
    function insertBeast(beastURL) {
        removeExistingBeasts();
        let beastImage = document.createElement("img");
        beastImage.setAttribute("src", beastURL);
        beastImage.style.height = "100vh";
        beastImage.className = "beastify-image";
        document.body.appendChild(beastImage);
    }
    
    /**
     * Remove every beast from the page.
     */
    function removeExistingBeasts() {
        let existingBeasts = document.querySelectorAll(".beastify-image");
        for (let beast of existingBeasts) {
            beast.remove();
        }
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
            element.classList.remove('anno-highlight-selected');
        });

        window.annoSelected = [];
    }

    function handleClick(event) {
        annoUnselectAll();

        window.annoSelected.push(event.target);
        event.target.classList.add('anno-highlight-selected');
        sidebarPort.postMessage({
            target: {
                source: window.location.href,
                selector: CssSelector(event.target)
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
        event.target.classList.add('anno-highlight-hover');
        event.stopPropagation();
    }
    
    function handleMouseOut(event) {
        event.target.classList.remove('anno-highlight-hover');
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
        }
        
        document.body.removeChild(dismissButton);
    }

    dismissButton.addEventListener('click', dismissOverlay);
    
    function applyOverlay() {
        document.body.appendChild(css);

        for (var element of document.body.querySelectorAll('*')) {
            element.originalOnmouseover = element.onmouseover;
            element.originalOnmouseout = element.onmouseout;
            element.originalOnclick = element.onclick;

            element.onmouseover = handleMouseOver;
            element.onmouseout = handleMouseOut;
            element.onclick = handleClick;
        }

        document.body.appendChild(dismissButton);
    }

    applyOverlay();
    
    
    /**
     * Listen for messages from the background script.
     * Call "beastify()" or "reset()".
     browser.runtime.onMessage.addListener((message) => {
        if (message.command === "beastify") {
            insertBeast(message.beastURL);
        } else if (message.command === "reset") {
            removeExistingBeasts();
        }
    });
    */
    
    
    // CSSPathTest();

    
})();
