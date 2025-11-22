import dayjs from 'dayjs';

export interface ICountry {
  id?: number;
  name?: string;
  code?: string | null;
  createdAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  active?: boolean | null;
}

export const defaultValue: Readonly<ICountry> = {
  active: false,
};
