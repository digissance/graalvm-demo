package biz.digissance.graalvmdemo.jpa.event;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import lombok.Data;

@Data
public class EntityCreated {
    private final BaseEntity entity;
}
