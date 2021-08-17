import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProjectFaComponent } from './list/project-fa.component';
import { ProjectFaDetailComponent } from './detail/project-fa-detail.component';
import { ProjectFaUpdateComponent } from './update/project-fa-update.component';
import { ProjectFaDeleteDialogComponent } from './delete/project-fa-delete-dialog.component';
import { ProjectFaRoutingModule } from './route/project-fa-routing.module';

@NgModule({
  imports: [SharedModule, ProjectFaRoutingModule],
  declarations: [ProjectFaComponent, ProjectFaDetailComponent, ProjectFaUpdateComponent, ProjectFaDeleteDialogComponent],
  entryComponents: [ProjectFaDeleteDialogComponent],
})
export class ProjectFaModule {}
