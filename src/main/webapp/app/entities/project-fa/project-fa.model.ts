import * as dayjs from 'dayjs';
import { ICustomFieldValueFa } from 'app/entities/custom-field-value-fa/custom-field-value-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ProjectStatus } from 'app/entities/enumerations/project-status.model';

export interface IProjectFa {
  id?: number;
  remoteId?: number | null;
  number?: string | null;
  contactName?: string | null;
  name?: string;
  description?: string | null;
  startDate?: dayjs.Dayjs;
  hoursEstimated?: number | null;
  hourlyRate?: number | null;
  status?: ProjectStatus;
  customFields?: ICustomFieldValueFa[] | null;
  contact?: IContactFa | null;
}

export class ProjectFa implements IProjectFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string | null,
    public contactName?: string | null,
    public name?: string,
    public description?: string | null,
    public startDate?: dayjs.Dayjs,
    public hoursEstimated?: number | null,
    public hourlyRate?: number | null,
    public status?: ProjectStatus,
    public customFields?: ICustomFieldValueFa[] | null,
    public contact?: IContactFa | null
  ) {}
}

export function getProjectFaIdentifier(project: IProjectFa): number | undefined {
  return project.id;
}
