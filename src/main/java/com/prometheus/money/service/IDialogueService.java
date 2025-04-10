package com.prometheus.money.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prometheus.money.entity.Dialogue;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-01
 */
public interface IDialogueService extends IService<Dialogue> {

	// 增：添加文档
	public void create(List<Dialogue> dialogueList);

	// 查：根据 ID 查询文档
	public Dialogue read(Integer id);

	// 改：更新文档
	public Dialogue update(Integer id, Dialogue updatedDialogue);

	// 删：删除文档
	public void delete(Integer id);
}
