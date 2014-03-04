<script type="text/template" id="communityRow">
<tr>
	<td style="width:120px; display: inline-block; word-wrap:break-word; padding-left: 25px;">
			<span dojoAttachPoint="placeLinkNode">
				<a href="javascript: void(0)" target="_self" title="${tooltip}" dojoAttachPoint="placeTitleLink" data-dojo-attach-event="onclick: handleClick">${title}</a>
			</span>
			<span style="display: none" class=lotusDivider role=presentation dojoAttachPoint="sourceTypeSectionDevider">|</span>
			<span style="display: none" class="lotusType commType" dojoAttachPoint="moderatedIconNode">
				<img style="display: inline" title="${nls.moderated}" alt="${nls.moderated}" src="${blankGif}">
				${nls.moderated} 
			</span>
			<span style="display: none" class="lotusType commType" dojoAttachPoint="restrictedIconNode">
				<img style="display: inline" class="iconsStates16 iconsStates16-CheckedOut" title="${nls.restricted}" alt="${nls.restricted}" src="${blankGif}">
				${nls.restricted} 
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
