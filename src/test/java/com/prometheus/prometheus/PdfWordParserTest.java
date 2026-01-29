package com.prometheus.prometheus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.prometheus.money.PrometheusMoneyApplication;
import com.prometheus.money.entity.Cet;
import com.prometheus.money.mapper.CetMapper;
@SpringBootTest(classes = PrometheusMoneyApplication.class)
@ActiveProfiles("dev")
public class PdfWordParserTest {
	@Autowired
	private CetMapper cetMapper;
	
	@Test
    void cetMain() {
    	for(int i=1;i<52;i++) {
    		String path = "";
    		//if(i<10) {
    			path = path+"0"+i;
    		
    		String filename ="202507"+path;
            // 替换为你的 PDF 文件路径
            String filePath = "‪C:\\Users\\MSI_NB\\Desktop\\output\\cet\\6\\202507"+path+".pdf"; 
            filePath = filePath.replaceAll("[\\p{Cf}]", ""); 

         // 或者简单粗暴地去除首尾空白和不可见字符
         filePath = filePath.trim();
           
            try {
                // 1. 提取文本
                String rawText = extractTextFromPdf(filePath);
                //System.out.println(rawText);
                
                if (rawText != null) {
                   // System.out.println("--- 提取完成，开始解析单词 ---");
                    
                    // 2. 解析并统计单词
                   List<String> wordCounts = parseAndCountWords(rawText);
                   for(String word:wordCounts){
                	   Cet cet = new Cet();
                	   cet.setFilename(filename);
                	   cet.setWord(word);
                	   cetMapper.insert(cet);
                   }
                }

            } catch (IOException e) {
                System.err.println("读取 PDF 文件失败: " + e.getMessage());
            }
    		
    	}

    }

    /**
     * 使用 PDFBox 从 PDF 中提取全部文本
     */
    String extractTextFromPdf(String filePath) throws IOException {
        File file = new File(filePath);
        // 关键点：load 方法支持传入密码。"" 代表尝试以空密码打开
        try (PDDocument document = PDDocument.load(file, "")) {
            
            // 如果文档被加密了，尝试去掉它的保护层
            if (document.isEncrypted()) {
                try {
                    // 尝试移除安全限制
                    document.setAllSecurityToBeRemoved(true);
                } catch (Exception e) {
                    System.err.println("无法解除加密限制: " + e.getMessage());
                    return null;
                }
            }

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    /**
     * 解析文本，提取单词并统计频率
     */
     List<String> parseAndCountWords(String text) {
        List<String> wordList = new ArrayList<>();

        // 正则表达式：[a-zA-Z]+ 表示匹配连续的英文字母
        // 如果你需要包含连字符的单词（如 well-known），可以使用 "[a-zA-Z]+(-[a-zA-Z]+)*"
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(text);
        
        while (matcher.find()) {
        	//System.out.println(matcher);
            // 获取单词并转为小写，以便统一统计 (例如 The 和 the 视为同一个词)
            String word = matcher.group().toLowerCase();
            // 过滤掉太短的单词（可选）
            if (word.length() > 1) {
            	wordList.add(word);
            }
        }
        return wordList;
    }
}
