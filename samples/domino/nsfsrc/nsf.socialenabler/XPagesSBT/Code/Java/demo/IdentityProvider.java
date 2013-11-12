package demo;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.social.impl.IdentityMapper;

public class IdentityProvider implements IdentityMapper {

	public String getUserIdFromIdentity(String target, String identity) {
		if(StringUtil.equals(target,"facebook")) {
			if(StringUtil.equals(identity,"fadams@facebook.com")) {
				return "CN=Frank Adams/O=renovations";
			}
		}
		return null;
	}

	public String getUserIdentityFromId(String target, String id) {
		if(StringUtil.equals(target,"facebook")) {
			if(StringUtil.equals(id,"CN=Frank Adams/O=renovations")) {
				return "fadams@facebook.com";
			}
		}
		return null;
	}

}
