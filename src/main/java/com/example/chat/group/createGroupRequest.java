package com.example.chat.group;

import jakarta.validation.constraints.NotEmpty;

public record createGroupRequest (@NotEmpty String name,
                                  String profile){
}
