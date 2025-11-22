export interface IEntityType {
  id?: number;
  name?: string;
  label?: string | null;
}

export const defaultValue: Readonly<IEntityType> = {};
