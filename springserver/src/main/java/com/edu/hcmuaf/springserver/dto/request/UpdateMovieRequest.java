package com.edu.hcmuaf.springserver.dto.request;

import com.edu.hcmuaf.springserver.entity.Category;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateMovieRequest {
    private String background_img_url;
    private String title_img_url;
    private String title;
    private Date released_date;
    private String trailer_video_url;
    private String poster_url;
    private String description;
    private String sub_title;
    private String age_type;
    private String type;
    private List<Category> categories;
    private int is_active;
}
