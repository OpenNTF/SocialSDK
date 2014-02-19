
<script type="text/template" id="communities-grid-row-template">
<tr class="${rowClass}" >
	<?php 
		if (isset($instance['ibm-sbtk-communities-icon']) && $instance['ibm-sbtk-communities-icon'] == 'icon') {
	?>
		<td class=lotusFirstCell width=35 dojoAttachPoint="communityIcon">
			<img role=presentation alt="" src="${logoUrl}" width=64 height=64>
		</td>   
	<?php
		}
	?> 

	<td>
		<h4>
			<span dojoAttachPoint="placeLinkNode">
				<a href="javascript: void(0)" target="_self" title="${tooltip}" dojoAttachPoint="placeTitleLink" data-dojo-attach-event="onclick: handleClick">${title}</a>
			</span>
			<span dojoAttachPoint="sourceTypePlaceHolder"></span>
			<span style="display: none" dojoAttachPoint="sourceTypeSectionDevider">|</span>
			<span style="display: none" dojoAttachPoint="moderatedIconNode">
				<img style="display: inline" class="lconnSprite lconnSprite-iconModeratedCommunity16" title="${nls.moderated}" alt="${nls.moderated}" src="${blankGif}">
				${nls.moderated} 
			</span>
			<span style="${displayRestricted}" dojoattachpoint="restrictedIconNode">
				<img class="iconsStates16 iconsStates16-CheckedOut" title="${nls.restricted}" alt="${nls.restricted}" src="${blankGif}" style="display: inline;">
			</span>
		</h4>
		<div>
			<span role=list>
			<?php 
				if (isset($instance['ibm-sbtk-communities-member-count']) && $instance['ibm-sbtk-communities-member-count'] == 'count') {
					?>
						<span role=listitem dojoAttachPoint="numOfMembersPlaceHolder">${numOfMembers}</span>
						<span role=presentation dojoAttachPoint="membersSectionDevider">|</span>
						<span role=listitem dojoAttachPoint="personPlaceHolder">
					
				    <?php
				}
			?> 
			<?php 
				if (isset($instance['ibm-sbtk-communities-updated-by']) && $instance['ibm-sbtk-communities-updated-by'] == 'updated') {
					?>
					${nls.updatedBy}
						<span class="vcard" dojoAttachPoint="vcardNode">
  							<a href="${getUserProfileHref}" data-dojo-attach-event="onclick: viewUserProfile" aria-label="${nls.ariaVcard}" class="fn url">${contributorName}</a>
  					    <span data-dojo-attach-point="idNode" class="x-lconn-userid" style="display: none;">${contributorUserid}</span>
				    	</span>
						</span> 
				
				<span role=presentation dojoAttachPoint="authorSectionDevider">|</span>
				<span role=listitem dojoAttachPoint="lastUpdateNode">${updatedDate}</span>    
				    <?php
				}
			?> 

				<?php 
				if (isset($instance['ibm-sbtk-communities-tags']) && $instance['ibm-sbtk-communities-tags'] == 'tags') {
					?>
						<span style="${displayTags}" class=lotusDivider role=presentation dojoAttachPoint="tagsSectionDevider">|</span>
						<span role=listitem dojoAttachPoint="tagsSection">
						${tagsLabel}
						${tagsAnchors}
						</span>      
				    <?php
				}
				?> 
			</span>
		</div>
	<?php 
		if (isset($instance['ibm-sbtk-communities-description']) && $instance['ibm-sbtk-communities-description'] == 'description') {
	?>
	<div dojoAttachPoint="detailsSection">
		<p dojoAttachPoint="placeDescNode">${summary}</p>
	</div>
	<div data-dojo-attach-point="customContent">
	</div>				
	<?php
	}
	?> 
	</td>
	<td>
	</td>
</tr>
</script>


