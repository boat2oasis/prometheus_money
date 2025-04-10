package com.prometheus.money.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.mapper.DialogueMapper;
import com.prometheus.money.repository.DialogueRepository;
import com.prometheus.money.service.IDialogueService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-01
 */
@Service
public class DialogueServiceImpl extends ServiceImpl<DialogueMapper, Dialogue> implements IDialogueService {
	@Autowired
	private DialogueRepository dialogueRepository;

	   // 增：添加文档
    public void create(List<Dialogue> dialogueList) {
         dialogueRepository.saveAll(dialogueList);
        
    }

    // 查：根据 ID 查询文档
    public Dialogue read(Integer id) {
        return dialogueRepository.findById(id).orElse(null);
    }

    // 改：更新文档
    public Dialogue update(Integer id, Dialogue updatedDialogue) {
        Dialogue existingDialogue = dialogueRepository.findById(id).orElse(null);
        if (existingDialogue != null) {
        	existingDialogue.setChinese("always lvoe");
        	existingDialogue.setSentence("zhou zhong ping");
    		
            return dialogueRepository.save(existingDialogue);
        }
        return null;
    }
    // 删：删除文档
    public void delete(Integer id) {
        dialogueRepository.deleteById(id);
    }

}
