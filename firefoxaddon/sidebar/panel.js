var myWindowId;

const annotationInput = document.getElementById('annotationInput');
const submitButton = document.getElementById('submitButton');

const annotationsList = document.getElementById('annotations');

/*
Make the content box editable as soon as the user mouses over the sidebar.
*/
window.addEventListener("mouseover", () => {
	annotationInput.setAttribute("contenteditable", true);
});

/*
When the user mouses out, save the current contents of the box.
*/
window.addEventListener("mouseout", () => {
	annotationInput.setAttribute("contenteditable", false);
});

// A URL parser, reference: https://stackoverflow.com/a/15979390
var urlParser = document.createElement('a');
function parseURL(url, value) {
    urlParser.href = url;
    return urlParser[value];
}

var allowedHosts = [
    "http://127.0.0.1:8000",
    "http://localhost:8000",
    "http://35.177.96.220"
];

/*
Update the sidebar's content.

1) Get the active tab in this sidebar's window.
2) Get its stored content.
3) Put it in the content box.
*/
function updateContent() {
	browser.tabs.query({windowId: myWindowId, active: true})
		.then((tabs) => {
            var url = tabs[0].url;
            
            if (allowedHosts.includes(parseURL(url, 'origin')))
                return browser.storage.local.get(url);
            else
                return new Promise((resolve, reject) => {
                    reject()
                });
		})
		.then(
            (storedInfo) => {
			    // annotationInput.textContent = storedInfo[Object.keys(storedInfo)[0]];
                annotationInput.textContent = "Supported website.";
                browser.tabs.executeScript({file: "/content_scripts/annotate_overlay.js"});
                // .then(listenForClicks)
                // .catch(reportExecuteScriptError);
            },
            (error) => {
                annotationInput.textContent = "Unsupported website.";
            }
        );
}

/*
Update content when a new tab becomes active.
*/
browser.tabs.onActivated.addListener(updateContent);

/*
Update content when a new page is loaded into a tab.
*/
browser.tabs.onUpdated.addListener(updateContent);

/*
When the sidebar loads, get the ID of its window,
and update its content.
*/
browser.windows.getCurrent({populate: true}).then((windowInfo) => {
	myWindowId = windowInfo.id;
	updateContent();
});

function appendAnnotation(annotation) {
    var div = document.createElement('div');
    div.classList.add('annotation');
    div.textContent = annotation.bodyValue;

    annotationsList.appendChild(div);
}

function updateAnnotations(annotations) {
    annotationsList.innerHTML = '';
    annotations.forEach(appendAnnotation);
}

function updateBinding(elementClicked) {
    updateAnnotations(elementClicked.annotations);
    annotationInput.textContent = '';
}

browser.runtime.onConnect.addListener(function (port) {
    if (port.name === "cs-sidebar") {
        port.onMessage.addListener(function (message) {
            if (message.elementClicked) {
                updateBinding(message.elementClicked);
            }
            else if (message.submissionSuccessful) {
                appendAnnotation(message.submissionSuccessful);

                annotationInput.textContent = '';
                annotationInput.setAttribute("contenteditable", true);
            }
        });

        submitButton.addEventListener('click', function (event) {
            var anno = { bodyValue: annotationInput.textContent };
            annotationInput.setAttribute("contenteditable", false);
            port.postMessage({ submitButtonClicked: anno });
        });
    }
});