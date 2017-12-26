var myWindowId;
const contentBox = document.querySelector("#content");

/*
Make the content box editable as soon as the user mouses over the sidebar.
*/
window.addEventListener("mouseover", () => {
	contentBox.setAttribute("contenteditable", true);
});

/*
When the user mouses out, save the current contents of the box.
*/
window.addEventListener("mouseout", () => {
	contentBox.setAttribute("contenteditable", false);
	browser.tabs.query({windowId: myWindowId, active: true}).then((tabs) => {
		let contentToStore = {};
		contentToStore[tabs[0].url] = contentBox.textContent;
		browser.storage.local.set(contentToStore);
	});
});


// A URL parser, reference: https://stackoverflow.com/a/15979390
var urlParser = document.createElement('a');

var allowedHosts = [
    "http://127.0.0.1:8000"
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
            urlParser.href = url;
            
            if (allowedHosts.includes(urlParser.origin))
                return browser.storage.local.get(url);
            else
                return new Promise((resolve, reject) => {
                    reject()
                });
		})
		.then(
            (storedInfo) => {
			    // contentBox.textContent = storedInfo[Object.keys(storedInfo)[0]];
                contentBox.textContent = "Supported website.";
                browser.tabs.executeScript({file: "/content_scripts/annotate_overlay.js"});
                // .then(listenForClicks)
                // .catch(reportExecuteScriptError);
            },
            (error) => {
                contentBox.textContent = "Unsupported website.";
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

function updateBinding(target) {
    contentBox.textContent = 'Now targeting: ' + JSON.stringify(target);
}

browser.runtime.onConnect.addListener(function (port) {
    if (port.name === "cs-to-sidebar") {
        port.onMessage.addListener(function (message) {
            updateBinding(message.target);
        });
    }
});