package com.hangyi.zd.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.eyunda.main.CommonListActivity;
import com.hy.client.R;


/**package base64;  
import java.awt.image.BufferedImage;    
import java.io.ByteArrayInputStream;    
import java.io.ByteArrayOutputStream;    
import java.io.File;    
import java.io.IOException;    
import javax.imageio.ImageIO;    
import sun.misc.BASE64Decoder;    
import sun.misc.BASE64Encoder;    
    
public class TestImageBinary {    
    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();    
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();    
        
    public static void main(String[] args) {    
            System.out.println(getImageBinary());  //将图片转成base64编码     
        base64StringToImage(getImageBinary()); //将base64的编码转成图片   
    }    
        
    static String getImageBinary(){    
        File f = new File("d://1.jpg");           
        BufferedImage bi;    
        try {    
            bi = ImageIO.read(f);    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();    
            ImageIO.write(bi, "jpg", baos);    
            byte[] bytes = baos.toByteArray();    
                
            return encoder.encodeBuffer(bytes).trim();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        return null;    
    }    
        
    static void base64StringToImage(String base64String){    
        try {    
            byte[] bytes1 = decoder.decodeBuffer(base64String);                  
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);    
            BufferedImage bi1 =ImageIO.read(bais);    
            File w2 = new File("d://2.png");//可以是jpg,png,gif格式    
            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
    }    
    
}*/
public class TestImageBase64Activity extends CommonListActivity {
	ImageView img;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.zd_testbase64);
		img = (ImageView) findViewById(R.id.img);
		String s = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCADwAUADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+f+iiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigDpPhr4Z1XXtVll07wlceL0tU/fWq211NHHuztZvs7I46HHzAHB617P4V+DEmp/CHxVrF/8H1t9d0yW0j0uxEGsKbsPJiYmM3G9gq4OVIxzmvne3t5LudIokeWWVgiIgLM5JwAAOpNfWvwBUfCbQ5vAMpU67d6TqGua2gYH7ExgCQW5/wBoIS7DsWHXt4OeVKlKnz05a6OybWkfels9rKzduqW7R+zeEmBwOOxbw2NorktKLnKNKaVSrF06aip0nJyTfPFc/wBiU7WUjxn4oeEvFafC+2vL/wCGun+E9GsLzylu4bCa3ulZh92QyyNK8ZLDDSAgMAqsPu11Pw8+DI1zwHc6zrHhLwbothBpH9p2l9dy6nOmoIrKjs/kXhaMjIJAjJyeFArzPwx4F1DUPg/4o8RW1/cwWem3FpZ3drGG2XaSsxBchsYR0jIDAglhyCOfpTR/FNh4P/Z70NNR1TSNHnvvBjw2ranCJopHa5XGYSkhkGCCQI245xiufMK1SjTVKk7y57O3NfWPN3d3t332uezwVlWAzLGTx+Y0nGksN7SKqexlF2quinpTpqME01GP7t3irycLX5/TP2fYdDa8P9nfC2LXbWWwhs4Ft9YvYHe9YpEZPPl8sdyMxyDj5gvGfMfBXgq68afH/U/DL+F/Bg1a7uZ0NtcXF5HYWDwq7yLGbebcAdpGCWAOANor3lfj14U8a+NLWz0/X9K1C41TVvDyWcNvpU1vNvguB5xeRoEBXB+QFjgbgAucHzf4G/8AKQjU/wDsL6z/AOgXNcWGxOKjTrzrXTUJNXutoxemztzX/K+59Rn2ScP1sdlOGyt05UqmKp0p+z9nKLU6tSDulzxU/Z8lrW0cZON7W2NB+B/hnxL4D8PvZaT8O7jxF4qvp00xvtevR2M9vCv7wYLeYJAwb7xVcAY3GuK+KHh9/BvwYF5baP8ADOTR9avXsUuNMt9Qkvo5oyGZllu/nQL5RXAbafMb5Wzkev8A7Nmq21l4H+C9vLN5c91caqIU/suC58whnJ/fOwkgwO8YJbocCvLPi/8A8mh+Gv8AsZ7/AP8AQpKdGrVWKdOUm0p21bf2px/9sW3W/olnWV4D/V369QoU6dR4bmbhCEdXSws2na7d3XmnzWbjyrVLml4VRRRX2R/LIUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQBueCPiNrHw4up7jRbmGyu54zH9pFrFJPCCrKTFIyl4iQxG6MqffgVV0fxjq/h7W31Ow1XUrHUpCxe7t7l4p23fey6kMc9+eazaKzdKDbk0rvR+aO2OZYuMIUo1ZKNNtxXM7Rb3cVfRuyu1rodJrPxj8X+I9MmstQ8VeJL+zuBtlguNTmlikGc4ZWYg8gdadpnxp8ZaJYRWtl4t8TWlrAoSOGHVJ4441AwAFDYAA9K5mip+rUbcvKreiOj+3cy9p7b6xU5rWvzyvbe173t5HW/wDC/vHf/Q7eLv8AwcXH/wAXWZ4a+Iut+EPF51/T9SuIdaYysbxiJZWaVWWRiXByxDN83XJznPNYtFCw1JJpRVno9FquzFPPMynUhVniJuUGpRbnK8ZLVSi76NPVNao6eT42eM5RKG8XeJ2E7B5M6pOfMIxgn5uSMDr6D0qPxh8XvE/xB0Sx07XNd1HVrTTneWBbqYysrPjJLH5mPGBuJ2gkDGTXOUUlhqKakoK620Q6mfZnOnOjPEVHGatJOcrSV72avZq+tn113CiiitzygooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA//2Q==";
		String[] arr = s.split(",");
		if(arr.length!=2)
			return;
		byte[] decodedString = Base64.decode(arr[1], Base64.DEFAULT);
		Bitmap bitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

		img.setImageBitmap(bitMap);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("imagebase64");
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}
	

}
