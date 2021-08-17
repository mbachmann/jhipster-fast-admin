import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsrFaComponent } from './list/isr-fa.component';
import { IsrFaDetailComponent } from './detail/isr-fa-detail.component';
import { IsrFaUpdateComponent } from './update/isr-fa-update.component';
import { IsrFaDeleteDialogComponent } from './delete/isr-fa-delete-dialog.component';
import { IsrFaRoutingModule } from './route/isr-fa-routing.module';

@NgModule({
  imports: [SharedModule, IsrFaRoutingModule],
  declarations: [IsrFaComponent, IsrFaDetailComponent, IsrFaUpdateComponent, IsrFaDeleteDialogComponent],
  entryComponents: [IsrFaDeleteDialogComponent],
})
export class IsrFaModule {}
