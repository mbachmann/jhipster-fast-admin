import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OwnerMySuffixDetailComponent } from './owner-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('OwnerMySuffix Management Detail Component', () => {
    let comp: OwnerMySuffixDetailComponent;
    let fixture: ComponentFixture<OwnerMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OwnerMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ owner: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OwnerMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OwnerMySuffixDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load owner on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.owner).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
