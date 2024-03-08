package com.challenger.fridge.util;


import com.challenger.fridge.domain.Category;
import com.challenger.fridge.domain.Item;
import com.challenger.fridge.repository.CategoryRepository;
import com.challenger.fridge.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class DataLoadService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public void saveDataFromCSV(File file) {

        log.info("fileLoadCheck={}", file.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int dataCount = 0;
            Category rootCategory = Category.builder()
                    .categoryName("ROOT")
                    .build();
            categoryRepository.save(rootCategory);
            while ((line = reader.readLine()) != null) {
                System.out.println("line = " + line);
                dataCount++;
                String[] data = line.split(",");

                String categoryName = data[0];
                String foodData = data[1];
                Category category = null;

                if (!categoryRepository.existsByCategoryName(categoryName)) {
                    Category mainCategory = Category.builder()
                            .categoryName(categoryName)
                            .parentCategory(rootCategory)
                            .build();
                    category = categoryRepository.save(mainCategory);
                } else {
                    category = categoryRepository.findByCategoryName(categoryName);
                }
                Item item = Item.builder()
                        .itemName(foodData)
                        .category(category)
                        .build();
                itemRepository.save(item);


            }
            log.info("총 데이터 개수={}", dataCount);
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리
        }
    }

}
