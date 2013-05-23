package doc;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.faces.application.FacesMessage;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.TextUtil;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.xsp.component.UIInputEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

import doc.JsonTree.TreeModel;

public class DocAPI {

	public static class DocTreeModel implements TreeModel {
		public boolean isLeaf(ViewEntry ve) throws NotesException {
			String form = (String)ve.getColumnValues().get(1);
			return StringUtil.endsWithIgnoreCase(form, "API");
		}
	}
	
	public String getEntriesAsJson() throws NotesException, IOException {
		JsonTree tree = new JsonTree();
		return tree.generateAsStringHier(new DocTreeModel(), "AllDocumentation", true);
	}
//	public String getEntriesAsJson() throws NotesException, IOException {
//		return 	"[{'id':'_root','name':'_root','children':[{'id':'Authentication','name':'Authentication','children':[{'id':'Authentication_Basic_-_Connections','name':'Basic - Connections','children':[{'id':'DFBE67C3AAFEDD3585257B13004C9C09','name':'Get My Communities - Dialog','url':'DFBE67C3AAFEDD3585257B13004C9C09','jspUrl':'Authentication_Basic_-_Connections_Get_My_Communities_-_Dialog'},{'id':'714E35E9D5E986DC85257B13004C9C22','name':'Get My Communities - Main Window','url':'714E35E9D5E986DC85257B13004C9C22','jspUrl':'Authentication_Basic_-_Connections_Get_My_Communities_-_Main_Window'},{'id':'AE0F01D41D17E8F085257B13004C9C42','name':'Get My Communities - Popup','url':'AE0F01D41D17E8F085257B13004C9C42','jspUrl':'Authentication_Basic_-_Connections_Get_My_Communities_-_Popup'}]},{'id':'Authentication_OAuth_1.0_-_Smartcloud','name':'OAuth 1.0 - Smartcloud','children':[{'id':'36E20AA367488DAA85257B13004C9C5A','name':'Read Profile - Main Window','url':'36E20AA367488DAA85257B13004C9C5A','jspUrl':'Authentication_OAuth_1.0_-_Smartcloud_Read_Profile_-_Main_Window'},{'id':'B3D303D18D9EFE3B85257B13004C9C73','name':'Read Profile - Popup','url':'B3D303D18D9EFE3B85257B13004C9C73','jspUrl':'Authentication_OAuth_1.0_-_Smartcloud_Read_Profile_-_Popup'}]},{'id':'Authentication_OAuth_2.0_-_Connections','name':'OAuth 2.0 - Connections','children':[{'id':'68C933C93B0ED87C85257B13004C9CE2','name':'Get My Files - Main Window','url':'68C933C93B0ED87C85257B13004C9CE2','jspUrl':'Authentication_OAuth_2.0_-_Connections_Get_My_Files_-_Main_Window'},{'id':'0F80B3A65231839C85257B13004C9D13','name':'Get My Files - Popup','url':'0F80B3A65231839C85257B13004C9D13','jspUrl':'Authentication_OAuth_2.0_-_Connections_Get_My_Files_-_Popup'}]},{'id':'Authentication_OAuth_2.0_-_Smartcloud','name':'OAuth 2.0 - Smartcloud','children':[{'id':'E181A4218A90B1CB85257B13004C9D2C','name':'Read Profile OAuth2 - Main Window','url':'E181A4218A90B1CB85257B13004C9D2C','jspUrl':'Authentication_OAuth_2.0_-_Smartcloud_Read_Profile_OAuth2_-_Main_Window'},{'id':'7DD97463EDB21AAB85257B13004C9D41','name':'Read Profile OAuth2 - Popup','url':'7DD97463EDB21AAB85257B13004C9D41','jspUrl':'Authentication_OAuth_2.0_-_Smartcloud_Read_Profile_OAuth2_-_Popup'}]}]},{'id':'Connections','name':'Connections','children':[{'id':'Connections_Activities','name':'Activities','children':[{'id':'Connections_Activities_Rest','name':'Rest','children':[{'id':'2AEA142CA29EC9F385257B13004C9D7A','name':'Get My Activities','url':'2AEA142CA29EC9F385257B13004C9D7A','jspUrl':'Connections_Activities_Rest_Get_My_Activities'},{'id':'AEA769CD6A31C9B585257B13004C9D95','name':'Get My Activities XML','url':'AEA769CD6A31C9B585257B13004C9D95','jspUrl':'Connections_Activities_Rest_Get_My_Activities_XML'}]}]},{'id':'Connections_ActivityStreams','name':'ActivityStreams','children':[{'id':'9FB9EB79008E90E485257B13004C9DB7','name':'Communities I Follow','url':'9FB9EB79008E90E485257B13004C9DB7','jspUrl':'Connections_ActivityStreams_Communities_I_Follow'},{'id':'FE7EF0215C1F9D6585257B13004C9DE6','name':'Create Activity','url':'FE7EF0215C1F9D6585257B13004C9DE6','jspUrl':'Connections_ActivityStreams_Create_Activity'},{'id':'08EC7AFE953B73CB85257B13004C9E10','name':'Create Activity with EE','url':'08EC7AFE953B73CB85257B13004C9E10','jspUrl':'Connections_ActivityStreams_Create_Activity_with_EE'},{'id':'BE15C087A639CE1985257B13004C9E47','name':'My Network Updates','url':'BE15C087A639CE1985257B13004C9E47','jspUrl':'Connections_ActivityStreams_My_Network_Updates'},{'id':'331BF37E33FF813785257B13004C9E7A','name':'My Status Updates','url':'331BF37E33FF813785257B13004C9E7A','jspUrl':'Connections_ActivityStreams_My_Status_Updates'},{'id':'4C5A19F8CE27376E85257B13004C9ECD','name':'People I Follow','url':'4C5A19F8CE27376E85257B13004C9ECD','jspUrl':'Connections_ActivityStreams_People_I_Follow'},{'id':'5A6DC1055EDCA5A985257B13004CA00D','name':'Top 5 Public Updates','url':'5A6DC1055EDCA5A985257B13004CA00D','jspUrl':'Connections_ActivityStreams_Top_5_Public_Updates'},{'id':'511706FA190E765685257B13004CA01E','name':'Updates From a Community','url':'511706FA190E765685257B13004CA01E','jspUrl':'Connections_ActivityStreams_Updates_From_a_Community'}]},{'id':'Connections_Bookmarks','name':'Bookmarks','children':[{'id':'Connections_Bookmarks_Rest','name':'Rest','children':[{'id':'75F1D937EF57B8EC85257B13004CA034','name':'Get All Bookmarks List','url':'75F1D937EF57B8EC85257B13004CA034','jspUrl':'Connections_Bookmarks_Rest_Get_All_Bookmarks_List'},{'id':'75B1CA2E7164813585257B13004CA045','name':'Get All Bookmarks XML','url':'75B1CA2E7164813585257B13004CA045','jspUrl':'Connections_Bookmarks_Rest_Get_All_Bookmarks_XML'}]}]},{'id':'Connections_Communities','name':'Communities','children':[{'id':'EC0288659968912185257B13004CA071','name':'Add Community Member','url':'EC0288659968912185257B13004CA071','jspUrl':'Connections_Communities_Add_Community_Member'},{'id':'F4F331A570330D8885257B13004CA099','name':'Create Community','url':'F4F331A570330D8885257B13004CA099','jspUrl':'Connections_Communities_Create_Community'},{'id':'5B672C9F026E661585257B13004CA0C8','name':'Delete Community','url':'5B672C9F026E661585257B13004CA0C8','jspUrl':'Connections_Communities_Delete_Community'},{'id':'52F26C96945A438685257B13004CA113','name':'Get Community Member Name','url':'52F26C96945A438685257B13004CA113','jspUrl':'Connections_Communities_Get_Community_Member_Name'},{'id':'9C22D27EB85B39EE85257B13004CA13D','name':'Get Community Members','url':'9C22D27EB85B39EE85257B13004CA13D','jspUrl':'Connections_Communities_Get_Community_Members'},{'id':'A03BF8F9E733782D85257B13004CA167','name':'Get Community Tags','url':'A03BF8F9E733782D85257B13004CA167','jspUrl':'Connections_Communities_Get_Community_Tags'},{'id':'60973F45600A169485257B13004CA18E','name':'Get Community Title','url':'60973F45600A169485257B13004CA18E','jspUrl':'Connections_Communities_Get_Community_Title'},{'id':'FFF5421CF8533E7885257B13004CA1C0','name':'Get My Communities','url':'FFF5421CF8533E7885257B13004CA1C0','jspUrl':'Connections_Communities_Get_My_Communities'},{'id':'54BB3EB97654BAA185257B13004CA1F7','name':'Get Public Communities','url':'54BB3EB97654BAA185257B13004CA1F7','jspUrl':'Connections_Communities_Get_Public_Communities'},{'id':'4331E45D1F63788A85257B13004CA275','name':'Remove Community Member','url':'4331E45D1F63788A85257B13004CA275','jspUrl':'Connections_Communities_Remove_Community_Member'},{'id':'FD082DAC87D629A785257B13004CA32A','name':'Update Community','url':'FD082DAC87D629A785257B13004CA32A','jspUrl':'Connections_Communities_Update_Community'},{'id':'714828C7889B786585257B13004CA384','name':'Update Community Tags','url':'714828C7889B786585257B13004CA384','jspUrl':'Connections_Communities_Update_Community_Tags'},{'id':'Connections_Communities_Rest','name':'Rest','children':[{'id':'E938A884E91E67C085257B13004CA3B6','name':'Get My Communities XML','url':'E938A884E91E67C085257B13004CA3B6','jspUrl':'Connections_Communities_Rest_Get_My_Communities_XML'}]}]},{'id':'Connections_Files','name':'Files','children':[{'id':'579A24866C84A45085257B13004CA417','name':'Add Comment To File','url':'579A24866C84A45085257B13004CA417','jspUrl':'Connections_Files_Add_Comment_To_File'},{'id':'3160A9B8700C473185257B13004CA45A','name':'Add Comment To My File','url':'3160A9B8700C473185257B13004CA45A','jspUrl':'Connections_Files_Add_Comment_To_My_File'},{'id':'87BDFAAA2B33D49885257B13004CA47E','name':'Add Files to Folder','url':'87BDFAAA2B33D49885257B13004CA47E','jspUrl':'Connections_Files_Add_Files_to_Folder'},{'id':'F2ACDEC47DF4A97E85257B13004CA4A5','name':'Delete File','url':'F2ACDEC47DF4A97E85257B13004CA4A5','jspUrl':'Connections_Files_Delete_File'},{'id':'74110FEEFD92F5A985257B13004CA4C9','name':'Get File Comments','url':'74110FEEFD92F5A985257B13004CA4C9','jspUrl':'Connections_Files_Get_File_Comments'},{'id':'357AEDE1195B640A85257B13004CA4F4','name':'Get Files Shared By Me','url':'357AEDE1195B640A85257B13004CA4F4','jspUrl':'Connections_Files_Get_Files_Shared_By_Me'},{'id':'90F2336C2653B49D85257B13004CA518','name':'Get Files Shared With Me','url':'90F2336C2653B49D85257B13004CA518','jspUrl':'Connections_Files_Get_Files_Shared_With_Me'},{'id':'37079DD37B191B6685257B13004CA53C','name':'Get Folder','url':'37079DD37B191B6685257B13004CA53C','jspUrl':'Connections_Files_Get_Folder'},{'id':'F47C76AB97A08E5285257B13004CA562','name':'Get My Files','url':'F47C76AB97A08E5285257B13004CA562','jspUrl':'Connections_Files_Get_My_Files'},{'id':'F73DCB820FFB62AB85257B13004CA58A','name':'Lock File','url':'F73DCB820FFB62AB85257B13004CA58A','jspUrl':'Connections_Files_Lock_File'},{'id':'70BB06F3E122DC4085257B13004CA5B6','name':'Unlock File','url':'70BB06F3E122DC4085257B13004CA5B6','jspUrl':'Connections_Files_Unlock_File'},{'id':'1308A26CCFBFC7FF85257B13004CA5E0','name':'Update File','url':'1308A26CCFBFC7FF85257B13004CA5E0','jspUrl':'Connections_Files_Update_File'},{'id':'24366E2AFE542CEC85257B13004CA618','name':'Upload File','url':'24366E2AFE542CEC85257B13004CA618','jspUrl':'Connections_Files_Upload_File'}]},{'id':'Connections_Profiles','name':'Profiles','children':[{'id':'B176D9866C0C203285257B13004CA650','name':'Create Profile','url':'B176D9866C0C203285257B13004CA650','jspUrl':'Connections_Profiles_Create_Profile'},{'id':'A9DBD4965094468885257B13004CA68A','name':'Delete Profile','url':'A9DBD4965094468885257B13004CA68A','jspUrl':'Connections_Profiles_Delete_Profile'},{'id':'BAA43360DFD03A6985257B13004CA6B4','name':'Get About','url':'BAA43360DFD03A6985257B13004CA6B4','jspUrl':'Connections_Profiles_Get_About'},{'id':'6610F26A4D0F4CD485257B13004CA6DD','name':'Get About with email','url':'6610F26A4D0F4CD485257B13004CA6DD','jspUrl':'Connections_Profiles_Get_About_with_email'},{'id':'E5C79CD50A7598EA85257B13004CA71A','name':'Get About with userId','url':'E5C79CD50A7598EA85257B13004CA71A','jspUrl':'Connections_Profiles_Get_About_with_userId'},{'id':'097B3D52A88F9A3985257B13004CA741','name':'Get Address','url':'097B3D52A88F9A3985257B13004CA741','jspUrl':'Connections_Profiles_Get_Address'},{'id':'D176A253B9918D7085257B13004CA76B','name':'Get Department','url':'D176A253B9918D7085257B13004CA76B','jspUrl':'Connections_Profiles_Get_Department'},{'id':'480DE3397E5004B885257B13004CA79E','name':'Get Display Name','url':'480DE3397E5004B885257B13004CA79E','jspUrl':'Connections_Profiles_Get_Display_Name'},{'id':'E28AF5B838D57D1D85257B13004CA7EB','name':'Get Email','url':'E28AF5B838D57D1D85257B13004CA7EB','jspUrl':'Connections_Profiles_Get_Email'},{'id':'8874BD7957021B1F85257B13004CA810','name':'Get Id','url':'8874BD7957021B1F85257B13004CA810','jspUrl':'Connections_Profiles_Get_Id'},{'id':'673D833FA3FF908485257B13004CA835','name':'Get Phone Number','url':'673D833FA3FF908485257B13004CA835','jspUrl':'Connections_Profiles_Get_Phone_Number'},{'id':'EB9800EFF35F1E1285257B13004CA89E','name':'Get Profile Colleagues','url':'EB9800EFF35F1E1285257B13004CA89E','jspUrl':'Connections_Profiles_Get_Profile_Colleagues'},{'id':'CACD553BAC0C3F8B85257B13004CA8C7','name':'Get Profile URL','url':'CACD553BAC0C3F8B85257B13004CA8C7','jspUrl':'Connections_Profiles_Get_Profile_URL'},{'id':'8EA0058BEE7F036485257B13004CA8F2','name':'Get Pronunciation URL','url':'8EA0058BEE7F036485257B13004CA8F2','jspUrl':'Connections_Profiles_Get_Pronunciation_URL'},{'id':'AABE0D74A361F55485257B13004CA931','name':'Get Thumbnail URL','url':'AABE0D74A361F55485257B13004CA931','jspUrl':'Connections_Profiles_Get_Thumbnail_URL'},{'id':'5309D623C0AB1D9385257B13004CA958','name':'Get Title','url':'5309D623C0AB1D9385257B13004CA958','jspUrl':'Connections_Profiles_Get_Title'},{'id':'BCFECD5AC3EA1BAC85257B13004CA9DB','name':'Update Address','url':'BCFECD5AC3EA1BAC85257B13004CA9DB','jspUrl':'Connections_Profiles_Update_Address'},{'id':'A8A297ACBEB57ECE85257B13004CAA06','name':'Update Phone Number','url':'A8A297ACBEB57ECE85257B13004CAA06','jspUrl':'Connections_Profiles_Update_Phone_Number'},{'id':'1AB5CA9A3852D10A85257B13004CAA2C','name':'Update Profile Error','url':'1AB5CA9A3852D10A85257B13004CAA2C','jspUrl':'Connections_Profiles_Update_Profile_Error'},{'id':'C16388B7B58E204485257B13004CAA57','name':'Update Title','url':'C16388B7B58E204485257B13004CAA57','jspUrl':'Connections_Profiles_Update_Title'},{'id':'Connections_Profiles_Dijits','name':'Dijits','children':[{'id':'83FAF715949C08C785257B13004CAAB2','name':'Display One Profile','url':'83FAF715949C08C785257B13004CAAB2','jspUrl':'Connections_Profiles_Dijits_Display_One_Profile'},{'id':'B90A8BF080B3E93285257B13004CAAFE','name':'Display Profile','url':'B90A8BF080B3E93285257B13004CAAFE','jspUrl':'Connections_Profiles_Dijits_Display_Profile'}]},{'id':'Connections_Profiles_Rest','name':'Rest','children':[{'id':'DA02E9726BD34CF585257B13004CAB11','name':'Read Name','url':'DA02E9726BD34CF585257B13004CAB11','jspUrl':'Connections_Profiles_Rest_Read_Name'},{'id':'B598EF758CC8ABA885257B13004CAB24','name':'Read Name and Email','url':'B598EF758CC8ABA885257B13004CAB24','jspUrl':'Connections_Profiles_Rest_Read_Name_and_Email'},{'id':'A3B3CB4D2CFDBA4985257B13004CAB41','name':'Read Profile Photo','url':'A3B3CB4D2CFDBA4985257B13004CAB41','jspUrl':'Connections_Profiles_Rest_Read_Profile_Photo'},{'id':'D8C4B0917E5C74C785257B13004CAB64','name':'Read Profile XML','url':'D8C4B0917E5C74C785257B13004CAB64','jspUrl':'Connections_Profiles_Rest_Read_Profile_XML'}]}]}]},{'id':'controls','name':'controls','children':[{'id':'controls_grid','name':'grid','children':[{'id':'9DE052C8AA16C3B385257B13005A8C9D','name':'Grid','url':'9DE052C8AA16C3B385257B13005A8C9D','jspUrl':'controls_grid_Grid'},{'id':'E0F0E2BC5B44385085257B13005A8C9E','name':'TemplatedGridRow','url':'E0F0E2BC5B44385085257B13005A8C9E','jspUrl':'controls_grid_TemplatedGridRow'},{'id':'controls_grid_connections','name':'connections','children':[{'id':'controls_grid_connections_communities','name':'communities','children':[{'id':'1FD81191E42FA90B85257B13005A8C9F','name':'BootstrapCommunitiesGrid','url':'1FD81191E42FA90B85257B13005A8C9F','jspUrl':'controls_grid_connections_communities_BootstrapCommunitiesGrid'},{'id':'71D30B2F09867D3485257B13005A8CA0','name':'CommunityActionGrid','url':'71D30B2F09867D3485257B13005A8CA0','jspUrl':'controls_grid_connections_communities_CommunityActionGrid'},{'id':'818C7F557B080EF185257B13005A8CA1','name':'MyCommunitiesGrid','url':'818C7F557B080EF185257B13005A8CA1','jspUrl':'controls_grid_connections_communities_MyCommunitiesGrid'},{'id':'14B3E6347CB7F06785257B13005A8CA2','name':'PublicCommunitiesDijit','url':'14B3E6347CB7F06785257B13005A8CA2','jspUrl':'controls_grid_connections_communities_PublicCommunitiesDijit'},{'id':'3A6F509B77E2172B85257B13005A8CA3','name':'PublicCommunitiesGrid','url':'3A6F509B77E2172B85257B13005A8CA3','jspUrl':'controls_grid_connections_communities_PublicCommunitiesGrid'}]},{'id':'controls_grid_connections_files','name':'files','children':[{'id':'2D5E6BCD0A12BD1E85257B13005A8CA4','name':'FileComments','url':'2D5E6BCD0A12BD1E85257B13005A8CA4','jspUrl':'controls_grid_connections_files_FileComments'},{'id':'3A2DB06DFFC0566E85257B13005A8CA5','name':'FileShares','url':'3A2DB06DFFC0566E85257B13005A8CA5','jspUrl':'controls_grid_connections_files_FileShares'},{'id':'20D564FA387C8BF085257B13005A8CA6','name':'MyActiveFolders','url':'20D564FA387C8BF085257B13005A8CA6','jspUrl':'controls_grid_connections_files_MyActiveFolders'},{'id':'D9FEB235156C68AA85257B13005A8CA7','name':'MyFiles','url':'D9FEB235156C68AA85257B13005A8CA7','jspUrl':'controls_grid_connections_files_MyFiles'},{'id':'A3A5F7F27D68F9F085257B13005A8CA8','name':'MyFilesDijit','url':'A3A5F7F27D68F9F085257B13005A8CA8','jspUrl':'controls_grid_connections_files_MyFilesDijit'},{'id':'AC768D1FE920026F85257B13005A8CA9','name':'PinnedFiles','url':'AC768D1FE920026F85257B13005A8CA9','jspUrl':'controls_grid_connections_files_PinnedFiles'},{'id':'E275BAEE0DEFEC5C85257B13005A8CAA','name':'PinnedFolders','url':'E275BAEE0DEFEC5C85257B13005A8CAA','jspUrl':'controls_grid_connections_files_PinnedFolders'},{'id':'362EC4D18B622C6385257B13005A8CAB','name':'PublicFiles','url':'362EC4D18B622C6385257B13005A8CAB','jspUrl':'controls_grid_connections_files_PublicFiles'},{'id':'DB391B8B9F64143385257B13005A8CAC','name':'PublicFolders','url':'DB391B8B9F64143385257B13005A8CAC','jspUrl':'controls_grid_connections_files_PublicFolders'},{'id':'C375D63E03B5D7A385257B13005A8CAD','name':'RecycledFiles','url':'C375D63E03B5D7A385257B13005A8CAD','jspUrl':'controls_grid_connections_files_RecycledFiles'},{'id':'F3374233316B64CD85257B13005A8CAE','name':'SharedFolders','url':'F3374233316B64CD85257B13005A8CAE','jspUrl':'controls_grid_connections_files_SharedFolders'},{'id':'F3335AA942D830F285257B13005A8CAF','name':'VisibleFolders','url':'F3335AA942D830F285257B13005A8CAF','jspUrl':'controls_grid_connections_files_VisibleFolders'}]},{'id':'controls_grid_connections_profiles','name':'profiles','children':[{'id':'5C808084C48D493D85257B13005A8CB0','name':'Colleagues','url':'5C808084C48D493D85257B13005A8CB0','jspUrl':'controls_grid_connections_profiles_Colleagues'},{'id':'B98887ED8E0846C785257B13005A8CB1','name':'ConnectionsInCommon','url':'B98887ED8E0846C785257B13005A8CB1','jspUrl':'controls_grid_connections_profiles_ConnectionsInCommon'},{'id':'4F214983F8FA59F385257B13005A8CB2','name':'DirectReports','url':'4F214983F8FA59F385257B13005A8CB2','jspUrl':'controls_grid_connections_profiles_DirectReports'},{'id':'1B6DD4F08C70D8E085257B13005A8CB3','name':'ProfileDijit','url':'1B6DD4F08C70D8E085257B13005A8CB3','jspUrl':'controls_grid_connections_profiles_ProfileDijit'},{'id':'BC13EC05B80C547685257B13005A8CB4','name':'ReportingChain','url':'BC13EC05B80C547685257B13005A8CB4','jspUrl':'controls_grid_connections_profiles_ReportingChain'},{'id':'F0D5A7A1E3A9AA5D85257B13005A8CB5','name':'StatusUpdates','url':'F0D5A7A1E3A9AA5D85257B13005A8CB5','jspUrl':'controls_grid_connections_profiles_StatusUpdates'}]},{'id':'controls_grid_connections_search','name':'search','children':[{'id':'1DF0E49A18646CA885257B13005A8CB6','name':'SearchAll','url':'1DF0E49A18646CA885257B13005A8CB6','jspUrl':'controls_grid_connections_search_SearchAll'}]}]}]},{'id':'controls_vcard','name':'vcard','children':[{'id':'97D50A21C333F00A85257B13005A8CB7','name':'CommunityVCard','url':'97D50A21C333F00A85257B13005A8CB7','jspUrl':'controls_vcard_CommunityVCard'},{'id':'BB071E89AFA463D485257B13005A8CB8','name':'ProfileVCard','url':'BB071E89AFA463D485257B13005A8CB8','jspUrl':'controls_vcard_ProfileVCard'},{'id':'6B4DF9D76EF22FDC85257B13005A8CB9','name':'ProfileVCardEmail','url':'6B4DF9D76EF22FDC85257B13005A8CB9','jspUrl':'controls_vcard_ProfileVCardEmail'},{'id':'C3FA4AC1AACA82C585257B13005A8CBA','name':'ProfileVCardInline','url':'C3FA4AC1AACA82C585257B13005A8CBA','jspUrl':'controls_vcard_ProfileVCardInline'},{'id':'DBD25DD0573060FE85257B13005A8CBB','name':'ProfileVCards','url':'DBD25DD0573060FE85257B13005A8CBB','jspUrl':'controls_vcard_ProfileVCards'}]}]},{'id':'data','name':'data','children':[{'id':'9DA4E756891FC4CF85257B13005A8CBC','name':'AtomReadStore','url':'9DA4E756891FC4CF85257B13005A8CBC','jspUrl':'data_AtomReadStore'}]},{'id':'Smartcloud','name':'Smartcloud','children':[{'id':'Smartcloud_Communities','name':'Communities','children':[{'id':'DBDB4A864A297BC785257B13004CAB8B','name':'Get Community Members','url':'DBDB4A864A297BC785257B13004CAB8B','jspUrl':'Smartcloud_Communities_Get_Community_Members'},{'id':'87BB25705D6E6F6185257B13004CABB0','name':'Get Community Title','url':'87BB25705D6E6F6185257B13004CABB0','jspUrl':'Smartcloud_Communities_Get_Community_Title'},{'id':'6578560B0631BF3F85257B13004CABD6','name':'Get My Communities','url':'6578560B0631BF3F85257B13004CABD6','jspUrl':'Smartcloud_Communities_Get_My_Communities'},{'id':'4AB27F6A6BA74D3085257B13004CABFB','name':'Get Public Communities','url':'4AB27F6A6BA74D3085257B13004CABFB','jspUrl':'Smartcloud_Communities_Get_Public_Communities'}]},{'id':'Smartcloud_Files','name':'Files','children':[{'id':'A3A0FBA9DE4B376685257B13004CAC40','name':'List All My Files','url':'A3A0FBA9DE4B376685257B13004CAC40','jspUrl':'Smartcloud_Files_List_All_My_Files'},{'id':'9AF6854C2C9D996885257B13004CAC6D','name':'Read File Entry','url':'9AF6854C2C9D996885257B13004CAC6D','jspUrl':'Smartcloud_Files_Read_File_Entry'},{'id':'2609825DD79F1C9885257B13004CAC96','name':'Read File Entry With Parameters','url':'2609825DD79F1C9885257B13004CAC96','jspUrl':'Smartcloud_Files_Read_File_Entry_With_Parameters'},{'id':'4966DDB9471CFB6A85257B13004CACBE','name':'Search All My Files','url':'4966DDB9471CFB6A85257B13004CACBE','jspUrl':'Smartcloud_Files_Search_All_My_Files'},{'id':'35F86751B782A02985257B13004CACE8','name':'Search My Files With Filters and Pagination','url':'35F86751B782A02985257B13004CACE8','jspUrl':'Smartcloud_Files_Search_My_Files_With_Filters_and_Pagination'},{'id':'67AA97C07E94ED1C85257B13004CAD24','name':'Upload File','url':'67AA97C07E94ED1C85257B13004CAD24','jspUrl':'Smartcloud_Files_Upload_File'}]},{'id':'Smartcloud_Profiles','name':'Profiles','children':[{'id':'705993FBF25203DD85257B13004CAD49','name':'Get About','url':'705993FBF25203DD85257B13004CAD49','jspUrl':'Smartcloud_Profiles_Get_About'},{'id':'BEFD5FFEC0978C9A85257B13004CAD8F','name':'Get Address','url':'BEFD5FFEC0978C9A85257B13004CAD8F','jspUrl':'Smartcloud_Profiles_Get_Address'},{'id':'4C3DB72B55C2A61D85257B13004CADC7','name':'Get Country','url':'4C3DB72B55C2A61D85257B13004CADC7','jspUrl':'Smartcloud_Profiles_Get_Country'},{'id':'6CCBBFD2C3AB8F7885257B13004CADF0','name':'Get Department','url':'6CCBBFD2C3AB8F7885257B13004CADF0','jspUrl':'Smartcloud_Profiles_Get_Department'},{'id':'CFCE238C3EF6407685257B13004CAE17','name':'Get Id','url':'CFCE238C3EF6407685257B13004CAE17','jspUrl':'Smartcloud_Profiles_Get_Id'},{'id':'680695BF011D951785257B13004CAE3C','name':'Get Organization Id','url':'680695BF011D951785257B13004CAE3C','jspUrl':'Smartcloud_Profiles_Get_Organization_Id'},{'id':'D22E138002D91E8285257B13004CAE60','name':'Get Phone Number','url':'D22E138002D91E8285257B13004CAE60','jspUrl':'Smartcloud_Profiles_Get_Phone_Number'},{'id':'16281E20C078993785257B13004CAE84','name':'Get Profile Url','url':'16281E20C078993785257B13004CAE84','jspUrl':'Smartcloud_Profiles_Get_Profile_Url'},{'id':'AD53A5A0E472B43185257B13004CAECA','name':'Get Title','url':'AD53A5A0E472B43185257B13004CAECA','jspUrl':'Smartcloud_Profiles_Get_Title'},{'id':'A8DC35C43F40325D85257B13004CAEF0','name':'Read Profile With Cache','url':'A8DC35C43F40325D85257B13004CAEF0','jspUrl':'Smartcloud_Profiles_Read_Profile_With_Cache'}]}]},{'id':'store','name':'store','children':[{'id':'DBA24A1A2B8B627D85257B13005A8CBD','name':'AtomStore','url':'DBA24A1A2B8B627D85257B13005A8CBD','jspUrl':'store_AtomStore'}]},{'id':'Utilities','name':'Utilities','children':[{'id':'F5ACC2CA250A007C85257B13004CAF1B','name':'Email','url':'F5ACC2CA250A007C85257B13004CAF1B','jspUrl':'Utilities_Email'},{'id':'72BA70C90B81101D85257B13004CAF31','name':'XML Parser','url':'72BA70C90B81101D85257B13004CAF31','jspUrl':'Utilities_XML_Parser'},{'id':'6FEB7CD108D85EE385257B13004CAF44','name':'XPath Engine','url':'6FEB7CD108D85EE385257B13004CAF44','jspUrl':'Utilities_XPath_Engine'}]}]}]";
//	}
	
	public void moveUp(String noteID) throws Exception {
		swap(noteID,true);
	}
	public void moveDown(String noteID) throws Exception {
		swap(noteID,false);
	}
	private boolean swap(String noteID, boolean previous) throws Exception {
		// Is there a faster way?
		View view = ExtLibUtil.getCurrentDatabase().getView("AllDocumentation");
		//view.setAutoUpdate(false);
		ViewNavigator vn = view.createViewNav();		
		try {
			for(ViewEntry ve=vn.getFirst(); ve!=null; ve=vn.getNext(ve)) {
				if(ve.getNoteID().equals(noteID)) {
					int docIndent = ve.getIndentLevel();
					Document doc = ve.getDocument();
					ve = previous ? vn.getPrev(ve) : vn.getNext(ve);
					if(ve!=null) {
						Document other = ve.getDocument();
						if(ve.getIndentLevel()==docIndent) {
							Object ts = other.getItemValue("OrderTS");
							other.replaceItemValue("OrderTS",doc.getItemValue("OrderTS"));
							doc.replaceItemValue("OrderTS",ts);
							doc.save();
							other.save();
							view.refresh();
							return true;
						}
					}
					return false;
				}
			}
		} finally {
			vn.recycle();
		}
		return false;
	}
	
	public void remove(DominoDocument doc, String fieldName, int idx) throws Exception {
		String json = doc.getItemValueString(fieldName);
		if(!StringUtil.isSpace(json)) {
			List<Object> array = (List<Object>)JsonParser.fromJson(JsonJavaFactory.instance,json);
			if(idx<array.size()) {
				array.remove(idx);
				doc.replaceItemValue(fieldName, JsonGenerator.toJson(JsonJavaFactory.instance,array));
			}
		}
	}

	public boolean isEmpty(DominoDocument doc, String fieldName) throws Exception {
		if(doc!=null) {
			Item item = doc.getDocument().getFirstItem(fieldName);
			if(item!=null) {
				String content;
				MIMEEntity e = item.getMIMEEntity();
				if(e!=null) {
					content = e.getContentAsText();
				} else { 
					content = item.getText();
				}
				if(!StringUtil.isSpace(content)) {
					// Should handle empty json objects/arrays as well
					content = content.trim();
					return content.equals("[]") || content.equals("{}");
				}
			}
		}
		return true;
	}
	
	public String generateValueList(String list) {
		if(StringUtil.isEmpty(list)) {
			return "";
		}
		StringBuilder b = new StringBuilder();
		b.append("<ul>");
		String[] values = StringUtil.splitString(list, '\n');
		for(int i=0; i<values.length; i++) {
			String s = values[i];
			if(StringUtil.isNotEmpty(s)) {
				String name = s;
				String desc = null;
				int pos = s.indexOf('|');
				if(pos>=0) {
					name = s.substring(0,pos);
					desc = s.substring(pos+1);
				}
				b.append("<li>");
				b.append("<b>");
				b.append(TextUtil.toXMLString(name));
				b.append("</b>");
				if(StringUtil.isNotEmpty(desc)) {
					b.append(": ");
					b.append(TextUtil.toXMLString(desc));
				}
				b.append("</li>");
			}
		}
		b.append("</ul>");
		return b.toString();
	}
	
	public void prettify(UIInputEx c) {
		String value = c.getValueAsString();
		value = prettify(value,false);
		if(value!=null) {
			c.setValue(value);
		} else {
			FacesContextEx ctx = FacesContextEx.getCurrentInstance(); 
			String msg = "Invalid file content";
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
			ctx.addMessage(c != null ? c.getClientId(ctx) : null, m);
		}
	}
	public void compact(UIInputEx c) {
		String value = c.getValueAsString();
		value = prettify(value,true);
		if(value!=null) {
			c.setValue(value);
		} else {
			FacesContextEx ctx = FacesContextEx.getCurrentInstance(); 
			String msg = "Invalid file content";
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
			ctx.addMessage(c != null ? c.getClientId(ctx) : null, m);
		}
	}
	public String prettify(String source,boolean compact) {
		try {
			source = source.trim();
			if(source.startsWith("[") || source.startsWith("{")) {
				return prettifyJSON(source,compact);
			} else if(source.startsWith("<")) {
				return prettifyXML(source,compact);
			}
			return source;
		} catch(Exception e) {
			return null;
		}
	}
	private String prettifyJSON(String source,boolean compact) throws Exception{
		Object o = JsonParser.fromJson(JsonJavaFactory.instance,new StringReader(source));
		return JsonGenerator.toJson(JsonJavaFactory.instance, o, compact);
	}
	private String prettifyXML(String source,boolean compact) throws Exception{
		org.w3c.dom.Document d = DOMUtil.createDocument(source);
		return DOMUtil.getXMLString(d,compact);
	}
}
