package servicos.gratitude.be_gratitude_capacita.core.domain;

public record Pageable(
        int page,
        int size,
        String sortBy,
        String sortDirection) {
    public static Pageable of(int page, int size) {
        return new Pageable(page, size, null, null);
    }

    public static Pageable of(int page, int size, String sortBy, String sortDirection) {
        return new Pageable(page, size, sortBy, sortDirection);
    }

    public int getOffset() {
        return page * size;
    }
}