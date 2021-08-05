import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionFaDetailComponent } from './permission-fa-detail.component';

describe('Component Tests', () => {
  describe('PermissionFa Management Detail Component', () => {
    let comp: PermissionFaDetailComponent;
    let fixture: ComponentFixture<PermissionFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PermissionFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ permission: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PermissionFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PermissionFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load permission on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.permission).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
