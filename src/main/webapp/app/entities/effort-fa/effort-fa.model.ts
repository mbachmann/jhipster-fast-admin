import * as dayjs from 'dayjs';
import { IActivityFa } from 'app/entities/activity-fa/activity-fa.model';
import { ReportingEntityType } from 'app/entities/enumerations/reporting-entity-type.model';

export interface IEffortFa {
  id?: number;
  remoteId?: number | null;
  userId?: number | null;
  userName?: string | null;
  entityType?: ReportingEntityType | null;
  entityId?: number | null;
  duration?: number | null;
  date?: dayjs.Dayjs | null;
  activityName?: string | null;
  notes?: string | null;
  isInvoiced?: boolean | null;
  updated?: dayjs.Dayjs | null;
  hourlyRate?: number | null;
  activity?: IActivityFa | null;
}

export class EffortFa implements IEffortFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public userId?: number | null,
    public userName?: string | null,
    public entityType?: ReportingEntityType | null,
    public entityId?: number | null,
    public duration?: number | null,
    public date?: dayjs.Dayjs | null,
    public activityName?: string | null,
    public notes?: string | null,
    public isInvoiced?: boolean | null,
    public updated?: dayjs.Dayjs | null,
    public hourlyRate?: number | null,
    public activity?: IActivityFa | null
  ) {
    this.isInvoiced = this.isInvoiced ?? false;
  }
}

export function getEffortFaIdentifier(effort: IEffortFa): number | undefined {
  return effort.id;
}
