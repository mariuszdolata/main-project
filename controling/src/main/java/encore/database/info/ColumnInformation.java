package encore.database.info;

/*Klasa pomocnicza przechowuj¹ca informacjê o pojedynczej kolumnie w tabeli
 * ³atwa mo¿liwoœæ rozbudowy
 */
	public class ColumnInformation{
		
		/**
		 * 
		 */
		
		private String columnName;
		private String columnType;
		private boolean columnPK;
		private String tableCatalog;
		private String tableSchema;
		private String tableName;
		private int ordinalPosition;
		private String columnDefault;
		private boolean isNullable;
		private int characterMaximumLength;
		private int characterOctetLength;
		private int numericPrecision;
		private int numericScale;
		private int dateTimePrecision;
		private String characterSetName;
		private String collationName;
		private String extra;
		private String privileges;
		private String collumnComment;
		private String generationExpression;
		


		


		public ColumnInformation( String columnName, String columnType,
				boolean columnPK, String tableCatalog, String tableSchema, String tableName, int ordinalPosition,
				String columnDefault, boolean isNullable, int characterMaximumLength, int characterOctetLength,
				int numericPrecision, int numericScale, int dateTimePrecision, String characterSetName,
				String collationName, String extra, String privileges, String collumnComment,
				String generationExpression) {
			super();
			this.columnName = columnName;
			this.columnType = columnType;
			this.columnPK = columnPK;
			this.tableCatalog = tableCatalog;
			this.tableSchema = tableSchema;
			this.tableName = tableName;
			this.ordinalPosition = ordinalPosition;
			this.columnDefault = columnDefault;
			this.isNullable = isNullable;
			this.characterMaximumLength = characterMaximumLength;
			this.characterOctetLength = characterOctetLength;
			this.numericPrecision = numericPrecision;
			this.numericScale = numericScale;
			this.dateTimePrecision = dateTimePrecision;
			this.characterSetName = characterSetName;
			this.collationName = collationName;
			this.extra = extra;
			this.privileges = privileges;
			this.collumnComment = collumnComment;
			this.generationExpression = generationExpression;
		}



		public String getColumnName() {
			return columnName;
		}



		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}



		public String getColumnType() {
			return columnType;
		}



		public void setColumnType(String columnType) {
			this.columnType = columnType;
		}



		public boolean isColumnPK() {
			return columnPK;
		}



		public void setColumnPK(boolean columnPK) {
			this.columnPK = columnPK;
		}



		public String getTableCatalog() {
			return tableCatalog;
		}



		public void setTableCatalog(String tableCatalog) {
			this.tableCatalog = tableCatalog;
		}



		public String getTableSchema() {
			return tableSchema;
		}



		public void setTableSchema(String tableSchema) {
			this.tableSchema = tableSchema;
		}



		public String getTableName() {
			return tableName;
		}



		public void setTableName(String tableName) {
			this.tableName = tableName;
		}



		public int getOrdinalPosition() {
			return ordinalPosition;
		}



		public void setOrdinalPosition(int ordinalPosition) {
			this.ordinalPosition = ordinalPosition;
		}



		public String getColumnDefault() {
			return columnDefault;
		}



		public void setColumnDefault(String columnDefault) {
			this.columnDefault = columnDefault;
		}



		public boolean isNullable() {
			return isNullable;
		}



		public void setIsNullable(boolean isNullable) {
			this.isNullable = isNullable;
		}



		public int getCharacterMaximumLength() {
			return characterMaximumLength;
		}



		public void setCharacterMaximumLength(int characterMaximumLength) {
			this.characterMaximumLength = characterMaximumLength;
		}



		public int getCharacterOctetLength() {
			return characterOctetLength;
		}



		public void setCharacterOctetLength(int characterOctetLength) {
			this.characterOctetLength = characterOctetLength;
		}



		public int getNumericPrecision() {
			return numericPrecision;
		}



		public void setNumericPrecision(int numericPrecision) {
			this.numericPrecision = numericPrecision;
		}



		public int getNumericScale() {
			return numericScale;
		}



		public void setNumericScale(int numericScale) {
			this.numericScale = numericScale;
		}



		public int getDateTimePrecision() {
			return dateTimePrecision;
		}



		public void setDateTimePrecision(int dateTimePrecision) {
			this.dateTimePrecision = dateTimePrecision;
		}



		public String getCharacterSetName() {
			return characterSetName;
		}



		public void setCharacterSetName(String characterSetName) {
			this.characterSetName = characterSetName;
		}



		public String getCollationName() {
			return collationName;
		}



		public void setCollationName(String collationName) {
			this.collationName = collationName;
		}



		public String getExtra() {
			return extra;
		}



		public void setExtra(String extra) {
			this.extra = extra;
		}



		public String getPrivileges() {
			return privileges;
		}



		public void setPrivileges(String privileges) {
			this.privileges = privileges;
		}



		public String getCollumnComment() {
			return collumnComment;
		}



		public void setCollumnComment(String collumnComment) {
			this.collumnComment = collumnComment;
		}



		public String getGenerationExpression() {
			return generationExpression;
		}



		public void setGenerationExpression(String generationExpression) {
			this.generationExpression = generationExpression;
		}



		@Override
		public String toString() {
			return "ColumnInformation [columnName=" + columnName + ", columnType=" + columnType + ", columnPK="
					+ columnPK + ", tableName=" + tableName + "]";
		}



		public ColumnInformation() {
			
		}

	}