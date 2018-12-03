package com.rust.submit.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Rust
 */
public class HttpUtil {
	public static final String FETCH_ISS_LIST_URI = "http://badao.pinganfu.net/issue_badge/load_badge_contents";
	public static HttpGet touchHttpGetFetchIssList() {
		String param = "utf8=%E2%9C%93&f%5B%5D=spent_on&op%5Bspent_on%5D=t&f%5B%5D=user_id&op%5Buser_id%5D=%3D&v" +
				"%5Buser_id%5D%5B%5D=me&f%5B%5D=&c%5B%5D=project&c%5B%5D=spent_on&c%5B%5D=user&c%5B%5D=activity&c%5B" +
				"%5D=issue&c%5B%5D=comments&c%5B%5D=hours";
		HttpGet request = new HttpGet(FETCH_ISS_LIST_URI + "?" + param);
		request.addHeader("Cookie", "show_my_project=no; _redmine_session=SzZ5VmJVc3N4YUFMSEFqWDBtSWRkcFdmNDVQN2NreE1iY09VWGx3MWcrRlJITWtHWStHVHB3bTV3WENCS1dvNTVTby9TYzdWaWhJVUlkajlBN1l2K3Bxc1JIZWRob3ZpLzlhRDlsZkUzelg2aUpCN0FOaVhpNjI3YUlGNCtiOTRyU3dkc1NSYWR6ZzJtVXRLeVJFTHRuZkxCTUZwY29wV3ZoZ3ZLemdNNWlRQ1VPVVo2R1VlSi9mRWp0Y1hhS2VNSmprck1yOS9VSG1jekl2N3J1OFdubi9GZnpZb3o1ZG5SYk1YaS8xSWRnUDJ6bHVoMlJneEdjQkkwQnhDaW93Q3Fic3pmTUpDNkVRQnNUQmd2RldFdnZCVDc5ZUIvSFU5d2NNaWQrMlg0RmlGM0dUVXFnUjdMTXd6SWZNcVBhKzA1U2FGUnJnMjNpaVVkbVhneFlEdjJNdXo1L2s1TXBWejdkSlZaWDFJZHpIL3RsaStJdXdJMTcwWWNJYjRKeVVYbzdNa25RSUdyTjZ6cWVNTHhYMEt4bUpQOGk5NGpTZUdkYmJROWYxMGRLbkRqNk10MDFaQTVXNnVyaWd2UW94WXVIZW8zbWdRdXVvZkg4c2J5NFdnZnFwQUtXK1EwYlRtbldncWF0aFZiZEFEWTllMmp4OEFNWU1HUmlSOXJuMmphRncvZkcvcVhyVGZZVmdVUlRKN0JlVms5Mjc3N25DZzNCRWNmMXA2L1kyRUtpTUFqbnZoR2xQRk1Peno2cGwrVzFSZ3BhZkxjd2labXA4TlgrdDlGbzRlcndFeHRGWVM1RG9ReUhXeVRDU0RGbTVTREpQR3RGendHM3RNdG83Uy0tMGdPV3dTY1FocUs4b2FRaHZGVitJdz09--f3102b1b3c4bf3a51dcc2bab6a8473c0d8b29cc1");
		return request;
	}

	public static void main(String[] args) throws Exception {
		HttpGet httpGet = touchHttpGetFetchIssList();
		CloseableHttpClient aDefault = HttpClients.createDefault();
		HttpResponse response = aDefault.execute(httpGet);

		String string = EntityUtils.toString(response.getEntity());
		System.out.println(string);

		StringBuilder sb = new StringBuilder();
		sb.append(string);
		String[] split = string.split("\n");
		int start = 0;
		int end = 0;
		for (String s : split) {
			 start = sb.indexOf("<a href=\"/issues/")+
					"<a href=\"/issues/".length();
			 if (start<0) continue;
			String res = sb.substring(start, end = start + 6);
			if (!res.matches("[0-9]{6}")) break;
			sb.delete(0, start);
			System.out.println(res);
		}




	}
}
