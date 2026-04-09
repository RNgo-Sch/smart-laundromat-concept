package com.example.smart_laundromat_concept.data.remote.supabase;

public class SupabaseError {
    public String code;
    public String message;
    public String details;
    public String hint;
    public String error;   // for non-Postgres errors
}