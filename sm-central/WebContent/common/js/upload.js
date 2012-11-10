/**
* Updates the progress bar during file upload
**/
function refreshProgress()
{
    UploadMonitor.getUploadInfo(updateProgress);
}

function updateProgress(uploadInfo)
{

        document.getElementById('uploadbutton').disabled = true;
        //document.getElementById('file1').disabled = true;
        //document.getElementById('file2').disabled = true;
        //document.getElementById('file3').disabled = true;
        //document.getElementById('file4').disabled = true;

        var fileIndex = uploadInfo.fileIndex;

        var progressPercent = Math.ceil((uploadInfo.bytesRead / uploadInfo.totalSize) * 100);

        //document.getElementById('progressBarText').innerHTML = 'upload in progress: ' + progressPercent + '%';
        //document.getElementById('progressBarBoxContent').style.width = parseInt(progressPercent * 3.5) + 'px';


	    //myJsProgressBarHandler.setPercentage('element-progress',progressPercent);


        window.setTimeout('refreshProgress()', 1000);



    	return true;
}

function startProgress()
{

    //myJsProgressBarHandler.setPercentage('element-progress','0');
    document.getElementById('progressBar').style.display = 'block';
    document.getElementById('uploadproduct_button_label_submit').disabled = true;

    // wait a little while to make sure the upload has started ..
    //window.setTimeout("refreshProgress()", 1500);
    return true;

}

/**
function startPostProgress()
{
	document.getElementById('processBar').style.display = 'block';
	document.getElementById('processText').innerHTML = 'Processing file...';
	window.setTimeout("refreshPostProgress()", 1500);
      return true;


}

function updatePostProgress()
{
    if (!processInfo.done)
    {
    	window.setTimeout('refreshPostProgress()', 1000);
    } else {
    	document.getElementById('uploadbutton').disabled = false;
    	document.getElementById('processText').innerHTML = 'Done';
    }
    return true;

}

function refreshPostProgress()
{
	ProcessMonitor.getProcessInfo(updatePostProgress);
}
**/
