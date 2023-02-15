package ru.practicum.item;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final ItemRepository itemRepository;

    private final ItemUrlStatusProvider itemUrlStatusProvider;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository, @Lazy ItemUrlStatusProvider itemUrlStatusProvider){
        this.itemRepository = itemRepository;
        this.itemUrlStatusProvider = itemUrlStatusProvider;
    }

    @Override
    public List<ItemInfoWithUrlState> findAllByUserIdWithUrlState(Long userId) {
        List<ItemInfo> userUrls = itemRepository.findAllByUserId(userId);
        List<ItemInfoWithUrlState> checkedUrls = userUrls.stream()
                .map(info -> {
                    HttpStatus status = itemUrlStatusProvider.getItemUrlStatus(info.getId());
                    return new ItemInfoWithUrlState(info, status);
                })
                .collect(Collectors.toList());
        return checkedUrls;
    }
}
