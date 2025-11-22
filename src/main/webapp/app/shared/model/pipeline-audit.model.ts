import dayjs from 'dayjs';

export interface IPipelineAudit {
  id?: number;
  eventTimestamp?: dayjs.Dayjs | null;
  action?: string | null;
  rowId?: string | null;
  changes?: string | null;
  correlationId?: string | null;
}

export const defaultValue: Readonly<IPipelineAudit> = {};
