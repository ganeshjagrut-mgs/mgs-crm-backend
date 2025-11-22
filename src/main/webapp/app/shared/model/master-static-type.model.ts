export interface IMasterStaticType {
  id?: number;
  name?: string;
  description?: string | null;
  displayOrder?: number | null;
  isActive?: boolean | null;
}

export const defaultValue: Readonly<IMasterStaticType> = {
  isActive: false,
};
