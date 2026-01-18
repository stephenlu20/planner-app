package com.planner.api.entity;

public enum EventDeleteStrategy {
    KEEP_ALL,           // Don't delete anything
    DELETE_FUTURE,      // Delete future events only
    DELETE_ALL          // Delete all events
}