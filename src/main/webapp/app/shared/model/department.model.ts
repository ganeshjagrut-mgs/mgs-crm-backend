export interface IDepartment {
  id?: number;
  name?: string;
  code?: string | null;
  description?: string | null;
}

export const defaultValue: Readonly<IDepartment> = {};
