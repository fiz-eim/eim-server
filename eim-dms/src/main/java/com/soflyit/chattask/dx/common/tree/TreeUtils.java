package com.soflyit.chattask.dx.common.tree;

import com.soflyit.chattask.dx.modular.folder.domain.vo.FolderTree;
import com.soflyit.chattask.dx.modular.folder.domain.vo.TreeVO;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.common.core.utils.bean.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.tree
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-10  18:14
 * @Description:
 * @Version: 1.0
 */
public class TreeUtils {
    public static FolderTree toTree(List<FolderEntity> all, Long start) {
        FolderTree tree = null;
        Optional<FolderEntity> first = all.stream().filter(folder -> folder.getFolderParentId().equals(start)).findFirst();
        if (first.isPresent()) {
            FolderEntity item = first.get();
            tree = new FolderTree();
            tree.setFolderId(item.getFolderId());
            tree.setFolderName(item.getFolderName());
            tree.setFolderLevel(item.getFolderLevel());
            tree.setFolderParentId(item.getFolderParentId());
            tree.setNodes(findNodes(item, all));
        }
        return tree;
    }

    public static List<TreeVO> toList(List<FolderEntity> all, Long start) {
        List<TreeVO> list = new ArrayList<>();
        Optional<FolderEntity> first = all.stream().filter(folder -> folder.getFolderId().equals(start)).findFirst();
        if (first.isPresent()) {
            list.add(BeanUtils.convertBean(first.get(), TreeVO.class));
            getSubList(first.get(), list, all);
        }
        return list;
    }

    private static void getSubList(FolderEntity p, List<TreeVO> list, List<FolderEntity> all) {
        List<FolderEntity> subs = all.stream().filter(folder -> folder.getFolderParentId().equals(p.getFolderId())).collect(Collectors.toList());
        list.addAll(BeanUtils.convertList(subs, TreeVO.class));
        subs.forEach(item -> getSubList(item, list, all));
    }


    private static List<FolderTree> findNodes(FolderEntity root, List<FolderEntity> all) {
        List<FolderTree> list = new ArrayList<>();

        List<FolderEntity> items = all.stream().filter(folder -> folder.getFolderParentId().equals(root.getFolderId())).collect(Collectors.toList());
        items.forEach(item -> {
            FolderTree folderTree = new FolderTree();
            folderTree.setFolderId(item.getFolderId());
            folderTree.setFolderName(item.getFolderName());
            folderTree.setFolderLevel(item.getFolderLevel());
            folderTree.setFolderParentId(item.getFolderParentId());


            folderTree.setNodes(findNodes(item, all));
            list.add(folderTree);
        });
        return list;
    }


    public static FolderTree toTree(List<FolderEntity> all, List<Long> level) {
        List<FolderEntity> folders = all.stream().filter(item -> level.contains(item.getFolderId())).collect(Collectors.toList());


        FolderTree tree = null;
        Optional<FolderEntity> first = folders.stream().filter(folder -> folder.getFolderParentId().equals(level.get(0))).findFirst();
        if (first.isPresent()) {
            FolderEntity item = first.get();
            tree = new FolderTree();
            tree.setFolderId(item.getFolderId());
            tree.setFolderName(item.getFolderName());
            tree.setFolderLevel(item.getFolderLevel());
            tree.setFolderParentId(item.getFolderParentId());
            tree.setNodes(findNodes(item, folders));
        }
        return tree;
    }
}
