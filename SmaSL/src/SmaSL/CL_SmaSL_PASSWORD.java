package SmaSL;

import java.io.UnsupportedEncodingException;

public class CL_SmaSL_PASSWORD {
	private String gv_key;
	final private String code = "UTF8"; 
	public CL_SmaSL_PASSWORD(String Key) {
		gv_key = Key;
	}

	public String encript(String Str) {
		String EncriptStr = "";
		try {
			byte bStr[] = Str.getBytes(code);
			byte bKey[] = gv_key.getBytes(code);
			byte bEncriptStr[] = new byte[Str.length()];

			for (int i = 0; i < bStr.length; i++) {
				bEncriptStr[i] = (byte) (bStr[i] ^ bKey[i % bKey.length]);
			}
			EncriptStr = new String(bEncriptStr, code);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EncriptStr;
	}
	public String decript(String Str){		
		String EncriptStr = "";
		try{
		byte bStr[] = Str.getBytes(code);
		byte bKey[] = gv_key.getBytes(code);
		byte bEncriptStr[] = new byte[Str.length()];
		
		for (int i=0; i < bStr.length ; i++){
			bEncriptStr[i] = (byte)( bStr[i] ^ bKey[i % bKey.length ]);
		}
		EncriptStr = new String(bEncriptStr, code);
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EncriptStr;
	}
}
