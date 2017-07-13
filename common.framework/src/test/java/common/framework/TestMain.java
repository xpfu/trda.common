package common.framework;

import java.io.File;

import com.trda.common.utils.VerifyCodeUtils;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月12日 下午2:26:27
*/
public class TestMain {

	public static void main(String[] args) {
		File dir = new File("D:/verifies");
        int w = 200, h = 80;
        for (int i = 0; i < 1; i++) {
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            File file = new File(dir, verifyCode + ".jpg");
            try {
				VerifyCodeUtils.outputImage(w, h, file, verifyCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

	}

}
