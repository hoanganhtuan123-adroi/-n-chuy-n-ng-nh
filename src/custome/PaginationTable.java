package custome;

import model.PaymentModel;

import java.util.List;

public class PaginationTable {
    private final List<PaymentModel> data; // Dữ liệu gốc
    private final int pageSize;            // Số lượng bản ghi mỗi trang
    private int currentPage;               // Trang hiện tại

    public PaginationTable(List<PaymentModel> data, int pageSize) {
        this.data = data;
        this.pageSize = pageSize;
        this.currentPage = 0; // Bắt đầu từ trang đầu
    }

    public List<PaymentModel> getPageData() {
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, data.size());
        return data.subList(start, end);
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) data.size() / pageSize);
    }

    public void nextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
    }

    public int getCurrentPage() {
        return currentPage + 1; // Trang hiện tại (bắt đầu từ 1)
    }

    public void setCurrentPage(int page) {
        if (page >= 0 && page < getTotalPages()) {
            currentPage = page;
        }
    }
}
