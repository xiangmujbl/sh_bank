package com.mmec.css.mmec.element;

import java.util.HashMap;
import java.util.List;

import com.mmec.css.mmec.form.ElementForm;

public class WholeElement {
	private HashMap headForm;
	private List<ElementForm> elementList;

	public List<ElementForm> getElementList() {
		return elementList;
	}

	public void setElementList(List<ElementForm> elementList) {
		this.elementList = elementList;
	}

	public HashMap getHeadForm() {
		return headForm;
	}

	public void setHeadForm(HashMap headForm) {
		this.headForm = headForm;
	}

	/**
	 * 返回签名的数据原�? * @return
	 */
	public String getDataInput() {
		if (elementList.size() != 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < elementList.size(); i++) {
				ElementForm ent = (ElementForm) elementList.get(i);
				if (ent != null) {
					String name = ent.getName();
					if (name != null) {
						int x = name.lastIndexOf("/");
						name = name.substring(x + 1, name.length());
						String sha1 = ent.getSha1Digest();
						sb.append(name + "=" + sha1 + "&");
					}
				}
			}
			String data = sb.toString();
			return data;
		} else {
			return null;
		}
	}
}
