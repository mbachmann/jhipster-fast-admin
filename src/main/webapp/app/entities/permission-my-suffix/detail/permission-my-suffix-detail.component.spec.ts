import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionMySuffixDetailComponent } from './permission-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('PermissionMySuffix Management Detail Component', () => {
    let comp: PermissionMySuffixDetailComponent;
    let fixture: ComponentFixture<PermissionMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PermissionMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ permission: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PermissionMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PermissionMySuffixDetailComponent);
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
