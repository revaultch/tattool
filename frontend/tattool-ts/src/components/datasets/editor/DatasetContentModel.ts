class DatasetContentModel {
  content = {
    columns: ["col1", "col2", "col3"],
    rows: [["val1", "val2", "val3"]],
  } as any;

  colCount() {
    return this.content.columns.length;
  }

  addEmptyRow(index: number) {
    this.content.rows.splice(index, 0, Array(this.colCount()).fill(""));
  }

  addNewRow() {
    this.content.rows.splice(this.content.rows.length, 0, Array(this.colCount()).fill(""));
  }

  removeRow(index: number) {
    this.content.rows.splice(index, 1);
  }

  addColumn(index: number) {
    this.content.columns.splice(index, 0, "");
    this.content.rows.forEach((element: any) => {
      element.splice(index, 0, "");
    });
  }

  removeColumn(index: number) {
    this.content.columns.splice(index, 1);
    this.content.rows.forEach((element: any) => {
      element.splice(index, 1);
    });
  }
}

export { DatasetContentModel };
