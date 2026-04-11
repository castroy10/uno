package ru.castroy10.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private int id;
    private List<RowDto> rows;

    public int size() {
        return rows.size();
    }
}
