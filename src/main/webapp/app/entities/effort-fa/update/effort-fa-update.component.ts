import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEffortFa, EffortFa } from '../effort-fa.model';
import { EffortFaService } from '../service/effort-fa.service';
import { IActivityFa } from 'app/entities/activity-fa/activity-fa.model';
import { ActivityFaService } from 'app/entities/activity-fa/service/activity-fa.service';

@Component({
  selector: 'fa-effort-fa-update',
  templateUrl: './effort-fa-update.component.html',
})
export class EffortFaUpdateComponent implements OnInit {
  isSaving = false;

  activitiesSharedCollection: IActivityFa[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    userId: [],
    userName: [],
    entityType: [],
    entityId: [],
    duration: [],
    date: [],
    activityName: [],
    notes: [],
    isInvoiced: [],
    updated: [],
    hourlyRate: [],
    activity: [],
  });

  constructor(
    protected effortService: EffortFaService,
    protected activityService: ActivityFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ effort }) => {
      if (effort.id === undefined) {
        const today = dayjs().startOf('day');
        effort.updated = today;
      }

      this.updateForm(effort);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const effort = this.createFromForm();
    if (effort.id !== undefined) {
      this.subscribeToSaveResponse(this.effortService.update(effort));
    } else {
      this.subscribeToSaveResponse(this.effortService.create(effort));
    }
  }

  trackActivityFaById(index: number, item: IActivityFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEffortFa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(effort: IEffortFa): void {
    this.editForm.patchValue({
      id: effort.id,
      remoteId: effort.remoteId,
      userId: effort.userId,
      userName: effort.userName,
      entityType: effort.entityType,
      entityId: effort.entityId,
      duration: effort.duration,
      date: effort.date,
      activityName: effort.activityName,
      notes: effort.notes,
      isInvoiced: effort.isInvoiced,
      updated: effort.updated ? effort.updated.format(DATE_TIME_FORMAT) : null,
      hourlyRate: effort.hourlyRate,
      activity: effort.activity,
    });

    this.activitiesSharedCollection = this.activityService.addActivityFaToCollectionIfMissing(
      this.activitiesSharedCollection,
      effort.activity
    );
  }

  protected loadRelationshipsOptions(): void {
    this.activityService
      .query()
      .pipe(map((res: HttpResponse<IActivityFa[]>) => res.body ?? []))
      .pipe(
        map((activities: IActivityFa[]) =>
          this.activityService.addActivityFaToCollectionIfMissing(activities, this.editForm.get('activity')!.value)
        )
      )
      .subscribe((activities: IActivityFa[]) => (this.activitiesSharedCollection = activities));
  }

  protected createFromForm(): IEffortFa {
    return {
      ...new EffortFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      entityType: this.editForm.get(['entityType'])!.value,
      entityId: this.editForm.get(['entityId'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      date: this.editForm.get(['date'])!.value,
      activityName: this.editForm.get(['activityName'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      isInvoiced: this.editForm.get(['isInvoiced'])!.value,
      updated: this.editForm.get(['updated'])!.value ? dayjs(this.editForm.get(['updated'])!.value, DATE_TIME_FORMAT) : undefined,
      hourlyRate: this.editForm.get(['hourlyRate'])!.value,
      activity: this.editForm.get(['activity'])!.value,
    };
  }
}
