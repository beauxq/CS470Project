/* 
 * diable and enable checkboxes
 */

var title = document.getElementById("title");
var content = document.getElementById("content");
var tags = document.getElementById("tags");
var author = document.getElementById("author");

function titleClick()
{
    if (! title.checked)
    {
        content.checked = false;
        contentClick();
    }
    content.disabled = !(title.checked);
}

function contentClick()
{
    if (! content.checked)
    {
        tags.checked = false;
        tagsClick();
    }
    tags.disabled = !(content.checked);
}

function tagsClick()
{
    if (! tags.checked)
    {
        author.checked = false;
    }
    author.disabled = !(tags.checked);
}
