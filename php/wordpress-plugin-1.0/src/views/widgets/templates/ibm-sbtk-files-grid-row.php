
<script type="text/template" id="files-grid-row-template">
<tr>
    <td style="width:60%; word-wrap:break-word;">
        <h4 style="width:60%; word-wrap:break-word;">
            <a class="entry-title" rel="bookmark" title="${tooltip}" href="" data-dojo-attach-event="onclick: handleClick">${title}</a>    
        </h4>
        <div style="width:60%; word-wrap:break-word;">
            <ul>
                <li title=""  style="width:60%; word-wrap:break-word;">
				***REMOVED*** 
				if (isset($instance['ibm-sbtk-files-author']) && $instance['ibm-sbtk-files-author'] == 'author') {
					?>
					<span class="vcard" dojoAttachPoint="vcardNode"  style="width:60%; word-wrap:break-word;">
  						<a aria-label="${nls.ariaVcard}" href="${getUserProfileHref}" data-dojo-attach-event="onclick: viewUserProfile" class="fn url">${authorName}</a>
  						<span data-dojo-attach-point="idNode" class="x-lconn-userid" style="display: none;">${authorUserId}</span>
					</span>
				    ***REMOVED***
				}
				?> 
                   
				***REMOVED*** 
				if (isset($instance['ibm-sbtk-files-created']) && $instance['ibm-sbtk-files-created'] == 'created') {
					?>
						${_nls.created} ${createdLabel}
					
				    ***REMOVED***
				}
				?> 
                </li>
				***REMOVED*** 
				if (isset($instance['ibm-sbtk-files-downloads-count']) && $instance['ibm-sbtk-files-downloads-count'] == 'downloads') {
				?>
				<li>
                    ${hitCount} ${_nls.downloads}
                </li>
				***REMOVED***
				}
				?> 
				***REMOVED*** 
				if (isset($instance['ibm-sbtk-files-comments-count']) && $instance['ibm-sbtk-files-comments-count'] == 'comments') {
					?>	
                <li>
                    ${commentsCount} ${_nls.comments}
                </li>	
				***REMOVED***
				}
				?> 
            </ul>
        </div>
    </td>
</tr>
</script>


