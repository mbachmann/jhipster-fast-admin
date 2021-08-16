import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactRelationFaDetailComponent } from './contact-relation-fa-detail.component';

describe('Component Tests', () => {
  describe('ContactRelationFa Management Detail Component', () => {
    let comp: ContactRelationFaDetailComponent;
    let fixture: ComponentFixture<ContactRelationFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactRelationFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactRelation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactRelationFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactRelationFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactRelation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactRelation).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
