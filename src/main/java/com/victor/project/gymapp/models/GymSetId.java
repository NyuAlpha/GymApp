package com.victor.project.gymapp.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
public class GymSetId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "set_order", nullable = false)
    private Byte setOrder;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((exercise == null) ? 0 : exercise.hashCode());
        result = prime * result + ((setOrder == null) ? 0 : setOrder.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GymSetId other = (GymSetId) obj;
        if (exercise == null) {
            if (other.exercise != null)
                return false;
        } else if (!exercise.equals(other.exercise))
            return false;
        if (setOrder == null) {
            if (other.setOrder != null)
                return false;
        } else if (!setOrder.equals(other.setOrder))
            return false;
        return true;
    }




}
