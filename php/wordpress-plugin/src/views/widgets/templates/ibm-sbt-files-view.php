<script type="text/template" id="filesViewRow">
<tr>
	<td style="width:120px; display: inline-block; word-wrap:break-word; padding-left: 25px;">
			<span dojoAttachPoint="placeLinkNode">
				<input type="checkbox" data-dojo-attach-point="rowSelectionInput" data-dojo-attach-event="onclick: handleCheckBox" />
				<a href="javascript: void(0)" target="_self" title="${tooltip}" dojoAttachPoint="placeTitleLink" data-dojo-attach-event="onclick: handleClick">${title}</a>
			</span>
		<div>
			<hr style="width:85%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
		</div>
	</td>
</tr>
</script>
<script type="text/template" id="pagingHeader">
<div dojoAttachPoint="pagingHeader">
	<div>
		<hr style="width:90%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
	</div>
	<span dojoAttachPoint="showingResultsMessage">${pagingResults}</span>
			<span style="padding-left: 70px;">
				<a style="${hidePreviousLink};" title="${nls.previousPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: prevPage">${nls.previous}</a>
				<span style="${hidePreviousLabel}">${nls.previous}</span>
			</span>

			<a style="${hideNextLink} align: right;" title="${nls.nextPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: nextPage">${nls.next}</a>
			<span style="${hideNextLabel}">${nls.next}</span>
		<div>
			<hr style="width:90%; margin: 0.8em 0; left: -30px; border: 0; height: 1px;"/>
		</div>
</div>
</script>
<script type="text/template" id="pagingFooter">
<div dojoattachpoint="pagingFooter" class="lotusPaging" style="">
	<div>
		<hr style="width:90%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
	</div>
		Show:
			<a href="javascript: void(0)" title="${nls.show10Items}" aria-pressed="false"
				role="button" data-dojo-attach-event="onclick: show10ItemsPerPage">10</a> |

			<a href="javascript: void(0)"
				title="${nls.show25Items}" data-dojo-attach-event="onclick: show25ItemsPerPage"
				aria-pressed="false" role="button">25</a> |
	
			<a href="javascript: void(0)" title="${nls.show50Items}" data-dojo-attach-event="onclick: show50ItemsPerPage"
			aria-pressed="false" role="button">50</a> |
	
	
			<a href="javascript: void(0)" title="${nls.show100Items}" data-dojo-attach-event="onclick: show100ItemsPerPage"
			aria-pressed="false" role="button">100</a>
		 ${nls.items}
	<div>
		<hr style="width:90%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
	</div>
</div>
</script>

<script type="text/template" id="actionTemplate">
<span id="${id}">
<input type="button" value="${name}" class="btn" role="button" data-dojo-attach-point="actionNameNode"></input>
</span>
</script>

<script type="text/template" id="moveToTrashTemplate">
<div>
	<div dojoattachpoint="messageDiv">
		${moveToTrashMessage} 
	</div>
	<div style="padding-top:0.5em;">
		<strong>${fileName}</strong>
	</div>
</div>
</script>

<script type="text/template" id="uploadFileTemplate">
<div>
	<form >
	<table style="font-weight:700; margin:5px;">
		<tbody>
			<tr style="padding: 10px;">
				<td style="padding: 10px;">
					<div style="display: none;" dojoattachpoint="messageContainer"></div>
				</td>
			</tr>
			<tr style="padding: 10px;">
				<td style="padding: 10px;">
					<span>${nls.labelFile}</span>
				</td>
				<td style="padding: 10px;">
					<input type="file" name="_file" dojoattachpoint="fileInput"></input> 
				</td>
			</tr>
			<tr style="padding: 10px;">
				<td style="padding: 10px;">
					<span>${nls.labelTags}</span>
				</td>
				<td style="padding: 10px;">
					<input type="text" value="" name="_tags" dojoattachpoint="tagsInput" title="${nls.titleEnterTags}">
				</td>
				<td></td>
			</tr>
			<tr style="padding: 10px;">
				<td style="padding: 10px;">
					<span>${nls.labelShareWith}</span>
				</td>
				<td style="padding: 10px;">
					<fieldset>
						<span>${nls.labelNoone}</span>
						<input type="radio" name="_visibility" value="private" checked="" dojoattachpoint="privateInput">
						<span style="padding-left:0.5em;">${nls.labelPublic}</span>
						<input  style="padding: 10px;" type="radio" name="_visibility" value="public" title="${nls.titleShareEveryone}" dojoattachpoint="publicInput">

					</fieldset>
				
					<div>
						<input type="checkbox"  style="padding: 10px;"  name="_shareFilePropagate"	value="true" checked="" dojoattachpoint="propagateInput">
						<span>${nls.labelAllowOthers}</span>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="3" dojoattachpoint="buttonsNode">
					<button type="button" dojoattachevent="onclick: onExecute" dojoattachpoint="executeButton">${nls.labelUpload}</button>
					<button type="button" dojoattachevent="onclick: onCancel" dojoattachpoint="cancelButton">${nls.labelCancel}</button>
				</td>
			</tr>
		</tbody>
	</table>
	</form>
</div>
</script>

<script type="text/template" id="addTagsTemplate">
<div>	
	<form class="form-inline">
	<table cellpadding="0" class="table" role="presentation">
		<tbody>
			<tr>
				<td>
					<div dojoattachpoint="messageDiv">
						${nls.labelTags}
					</div>
					<div style="padding-top:0.5em;">
						<input style="padding-bottom:0.3em;" type="text" name="tags" dojoattachpoint="tagsInput" title="${nls.titleEnterTags}">
					</div>
				</td>
			</tr>
			<tr>
				<td dojoattachpoint="buttonsNode">
					<button type="button" class="btn" dojoattachevent="onclick: onExecute" dojoattachpoint="executeButton">${nls.moveToTrash}</button>
					<button type="button" class="btn" dojoattachevent="onclick: onCancel" dojoattachpoint="cancelButton">${nls.labelCancel}</button>
				</td>
			</tr>
		</tbody>
	</table>
	</form>
</div>
</div>
</script>

<script type="text/template" id="shareFilesTemplate">
<div>
	<table role="presentation" class="table">
		<tbody>
			<tr>
				<td>
					<span>${nls.labelShareWith} 
						<span role="presentation">&nbsp;</span>
					</span>
				</td>
				<td >
					<form action="">
						<input dojoAttachEvent="click: radioButtonSelected"  type="radio"  name="sbtRadioButton" value="${nls.labelPeople}" checked="true">
						<span>${nls.labelSharePeopleComms}</label><br>
						<input dojoAttachEvent="click: radioButtonSelected" type="radio" name="sbtRadioButton" value="${nls.labelPublic}" value="${nls.labelPublic}" >
						<span>${nls.labelPublic}</span>
					</form>
				</td>

			</tr>
			<tr dojoAttachPoint="shareWithRow">
				<td>
					Share with:<span role="presentation">&nbsp;</span>
				</td>
				

									<td>
										<span>Type</span>
										<select style="width:50%" name="searchSourceDropdown" title="Type" aria-label="Type">
											<option dojoAttachEvent="click: handleShareWithSelection" >${nls.labelAPerson}</option>
											<option dojoAttachEvent="click: handleShareWithSelection" >${nls.labelACommunity}</option>
										</select>
									
									<br /><span>${nls.labelRole}</span>
										<select style="width:50%" name="_shareFileUserRole" title="Role" aria-label="Role">
											<option dojoAttachEvent="click: handleSelectAccessType" value="${nls.labelAsReader}">${nls.labelAsReader}</option>
											<option dojoAttachEvent="click: handleSelectAccessType" value="${nls.labelAsEditor}">${nls.labelAsEditor}</option>
										</select>
									</td>

									<td>
										<div dojoAttachPoint="searchTypeAhead"></div>
									</td>


					<div  role="alert"></div>
				</td>
				
			</tr>
			<tr>
				<td>
					<div role="alert" style="display: none;"></div>
				</td>
			</tr>
			<tr style="display: none;" >
				<td>
					<span>${nls.labelReaders} </span></td>
				<td>
					<span>${nls.labelNone}</span>
				</td>
			</tr>

			<tr style="display: none;">
				<td><span>${nls.labelEditors} </span></td>
				<td>
					<span>${nls.labelNone}</span>
				</td>
			</tr>

			<tr dojoAttachPoint="makePublicWarning" style="display:none">
				<td>
					<div role="alert" style="">
						<span>${nls.shareWarning}</span>
					</div>
				</td>
			</tr>

			<tr dojoAttachPoint="messageRow">
				<td>
					<span>${nls.labelMessage}</span>
				</td>
				<td>
					<a dojoAttachEvent="click: setMessage" href="javascript:;" role="button">${nls.labelAddMessage}</a>
				</td>
			</tr>
			<tr dojoAttachPoint="messageTextArea" style="display: none;">
				<td>
					<span>${nls.labelMessage}</span></td>
				<td>
					<textarea dojoAttachPoint="textArea" name="_shareFileMessage"></textarea>
				</td>
			</tr>
		</tbody>
	</table>
</div>
</script>

<script type="text/template" id="dialogTemplate">
<div role="dialog" style="z-index:5000; -webkit-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5); -webkit-border-radius: 6px; -moz-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5); background:#CCC4C4; -moz-border-radius: 5px; color: black;" >
	<div dojoattachpoint="titleBar" style="display: none;">
		<span dojoattachpoint="titleNode"></span> 
		<span title="Cancel" dojoattachevent="onclick: onCancel" class="dijitDialogCloseIcon" dojoattachpoint="closeButtonNode"></span>
	</div>
	<div dojoattachpoint="containerNode">
		<div style="text-align:left; padding:10px; margin:13px;color:#666;">
			<form>
				<div style="padding-left:0.5em;padding-top:0.1em; padding-bottom:3em;" aria-label="${title}">
					<span><h1>${title}</h1></span>
					
				</div>
				<div>
				<div style="color:#666;">
					<div dojoattachpoint="contentNode"></div>
				</div>
				<div style="padding-bottom:0.5em; padding-left: 0.5em;" dojoattachpoint="buttonsNode">
					<input type="button" class="btn btn-primary" role="button" value="${nls.OK}" dojoattachevent="onclick: onExecute"  dojoattachpoint="executeButton">
					<input type="button" class="btn " role="button" value="${nls.Cancel}" dojoattachevent="onclick: onCancel" dojoattachpoint="cancelButton">
				</div>
			</form>
		</div>
	</div>
</div>
</script>

<script type="text/template" id="viewTemplate">
<div dojoAttachPoint="domNode">
	<div dojoAttachPoint="mainNode">
		<div role="alert" class="alert alert-success" style="z-index:5000; -webkit-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5); -webkit-border-radius: 6px; -moz-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5); background:#CCC4C4; -moz-border-radius: 5px; color: black; display: none;" dojoattachpoint="messageNode">
			<span class="alert alert-success" dojoattachpoint="messageBody"></span>
			&nbsp;<a dojoattachevent="onclick: hideMessage" title="${nls.root.messageClose}" dojoattachpoint="messageClose" role="button"  href="javascript:;">Close</a>
		</div>	
		<br/><br/>
		<div dojoAttachPoint="leftColNode" style="display:none;">
			<div>
				<div dojoAttachPoint="navMenu" id="navTree"></div>
			</div>
		</div>
		<div dojoAttachPoint="rightColNode" style="display:none;"></div>
		<div dojoAttachPoint="authNode" role="main" style="display:none;"></div>
		<div dojoAttachPoint="contentNode" role="main" ></div>
	</div>
</div>
</script>
