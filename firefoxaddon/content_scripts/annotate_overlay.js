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
        event.stopPropagation();
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
    
    function applyOverlay() {
        for (var element of document.querySelectorAll('*')) {
            element.addEventListener('mouseover', handleMouseOver);
            element.addEventListener('mouseout', handleMouseOut);

            element.addEventListener('click', handleClick);            
        }        
    }
    
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

    var css = document.createElement("style");
    css.type = "text/css";
    css.innerHTML = `
    .anno-highlight-hover {
        box-shadow: 0 0 2px 1px yellowgreen;
    }

    .anno-highlight-selected {
        box-shadow: 0 0 2px 4px tomato;
    }
    `;
    document.body.appendChild(css);
    
    // alert('woah')

    applyOverlay();
    
})();
